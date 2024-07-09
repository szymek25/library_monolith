package pl.szymanski.springfrontend.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.service.KeyCloakService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class KeycloakLogoutHandler implements LogoutHandler {

	@Autowired
	private KeyCloakService keyCloakService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response,
					   Authentication auth) {
		keyCloakService.logoutFromKeyCloak((OidcUser) auth.getPrincipal());
	}

}
