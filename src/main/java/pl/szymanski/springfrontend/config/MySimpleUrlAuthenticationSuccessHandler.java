package pl.szymanski.springfrontend.config;

import java.io.IOException;
import java.util.Collection;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.model.Department;
import pl.szymanski.springfrontend.model.User;
import pl.szymanski.springfrontend.service.UserService;

public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  @Resource
  private WebUtils webUtils;

  @Resource
  private UserService userService;

  @Override
  public void onAuthenticationSuccess(final HttpServletRequest request,
      final HttpServletResponse response, final Authentication authentication)
      throws IOException {

    handle(request, response, authentication);
    clearAuthenticationAttributes(request);
  }

  protected void handle(final HttpServletRequest request,
      final HttpServletResponse response, final Authentication authentication)
      throws IOException {

    final String targetUrl = determineTargetUrl(authentication);

    if (response.isCommitted()) {
      logger.debug(
          "Response has already been committed. Unable to redirect to "
              + targetUrl);
      return;
    }

    redirectStrategy.sendRedirect(request, response, targetUrl);
  }

  protected String determineTargetUrl(final Authentication authentication) {
    Collection<? extends GrantedAuthority> authorities
        = authentication.getAuthorities();
    boolean isManager = authorities.stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
            .equals(ApplicationConstants.MANAGER_ROLE_NAME));

    boolean isEmployee = authorities.stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
            .equals(ApplicationConstants.EMPLOYEE_ROLE_NAME));

    final User user = userService.getUserByEmail(authentication.getName());
    final Department department = user.getDepartment();
    boolean employeeHasIpAddressPermission = true;
    if (department != null && StringUtils.isNotEmpty(department.getIpAddress())) {
      employeeHasIpAddressPermission = department.getIpAddress().trim()
          .equals(webUtils.getClientIp().trim());
    }

    if (!employeeHasIpAddressPermission) {
      logger.warn(
          "Tried to login into department with ip restriction on address: {} from address: {}",
          department.getIpAddress(), webUtils.getClientIp());
      SecurityContextHolder.clearContext();
      return "/login?ipRestrict=true";
    } else if (isEmployee && department == null) {
      SecurityContextHolder.clearContext();
      logger.error("Employee {} has no department assigned", user.getEmail());
      return "/login?emplNonAssigned=true";
    }
    if (isManager) {
      return "/departments/list";
    } else {
      return "/books/list";
    }
  }

  protected void clearAuthenticationAttributes(final HttpServletRequest request) {
    final HttpSession session = request.getSession(false);
    if (session == null) {
      return;
    }
    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
  }

  public void setRedirectStrategy(final RedirectStrategy redirectStrategy) {
    this.redirectStrategy = redirectStrategy;
  }

  protected RedirectStrategy getRedirectStrategy() {
    return redirectStrategy;
  }
}
