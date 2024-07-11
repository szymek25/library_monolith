package pl.szymanski.springfrontend.oauth2;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.model.User;
import pl.szymanski.springfrontend.service.RoleService;
import pl.szymanski.springfrontend.service.UserService;

import java.sql.Date;
import java.util.Collection;
import java.util.Map;


@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {



	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Override
	public void onApplicationEvent(final InteractiveAuthenticationSuccessEvent event) {
		syncAccountData(event);
	}

	private void syncAccountData(final InteractiveAuthenticationSuccessEvent event) {
		final Authentication authentication = event.getAuthentication();
		final OidcUser user = (OidcUser) authentication.getPrincipal();
		final Map<String, Object> attributes = user.getAttributes();
		final String email = getStringAttribute(attributes, ApplicationConstants.KeyCloak.EMAIL);
		User userByEmail = userService.getUserByEmail(email);
		if (userByEmail == null) {
			userByEmail = new User();
			userByEmail.setEmail(email);
		}
		mapUserAttributes(userByEmail, attributes);
		userService.save(userByEmail);
	}

	private void mapUserAttributes(final User user, final Map<String, Object> attributes) {
		user.setEmail(getStringAttribute(attributes, ApplicationConstants.KeyCloak.EMAIL));
		user.setName(getStringAttribute(attributes, ApplicationConstants.KeyCloak.FIRST_NAME));
		user.setLastName(getStringAttribute(attributes, ApplicationConstants.KeyCloak.LAST_NAME));
		user.setAddressLine1(getStringAttribute(attributes, ApplicationConstants.KeyCloak.ADDRESS_LINE_1));
		user.setPhone(getStringAttribute(attributes, ApplicationConstants.KeyCloak.PHONE));
		user.setTown(getStringAttribute(attributes, ApplicationConstants.KeyCloak.TOWN));
		user.setPostalCode(getStringAttribute(attributes, ApplicationConstants.KeyCloak.POSTAL_CODE));
		user.setUuid(getStringAttribute(attributes, ApplicationConstants.KeyCloak.UUID));
		final String birthdate = getStringAttribute(attributes, ApplicationConstants.KeyCloak.BIRTHDATE);
		if (StringUtils.isNotEmpty(birthdate)) {
			user.setDayOfBirth(Date.valueOf(birthdate));
		}
		mapRole(user, attributes);
	}

	private void mapRole(User user, Map<String, Object> attributes) {
		if (attributes.containsKey("roles")) {
			final Collection<String> roles = (Collection<String>) attributes.get("roles");
			if (roles.size()==1 ) {
				String role = roles.stream().iterator().next();
				user.setRole(roleService.getByName(role));
			} else {
				throw new IllegalArgumentException("User can have only one role");
			}
		}
	}

	private String getStringAttribute(final Map<String, Object> attributes, final String key) {
		if (attributes.containsKey(key)) {
			return (String) attributes.get(key);
		}
		return StringUtils.EMPTY;
	}
}
