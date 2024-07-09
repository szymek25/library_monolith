package pl.szymanski.springfrontend.oauth2;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.model.User;
import pl.szymanski.springfrontend.service.UserService;

import java.sql.Date;
import java.util.Map;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	private static final String EMAIL = "email";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String ADDRESS_LINE_1 = "addressLine1";
	private static final String PHONE = "phone";
	private static final String BIRTHDATE = "birthdate";
	private static final String TOWN = "town";
	private static final String POSTAL_CODE = "postalCode";
	private static final String UUID = "sub";

	@Autowired
	private UserService userService;

	@Override
	public void onApplicationEvent(final InteractiveAuthenticationSuccessEvent event) {
		syncAccountData(event);
	}

	private void syncAccountData(final InteractiveAuthenticationSuccessEvent event) {
		final Authentication authentication = event.getAuthentication();
		final OidcUser user = (OidcUser) authentication.getPrincipal();
		final Map<String, Object> attributes = user.getAttributes();
		final String email = getStringAttribute(attributes, EMAIL);
		User userByEmail = userService.getUserByEmail(email);
		if (userByEmail == null) {
			userByEmail = new User();
			userByEmail.setEmail(email);
		}
		mapUserAttributes(userByEmail, attributes);
		userService.save(userByEmail);
	}

	private void mapUserAttributes(final User user, final Map<String, Object> attributes) {
		user.setEmail(getStringAttribute(attributes, EMAIL));
		user.setName(getStringAttribute(attributes, FIRST_NAME));
		user.setLastName(getStringAttribute(attributes, LAST_NAME));
		user.setAddressLine1(getStringAttribute(attributes, ADDRESS_LINE_1));
		user.setPhone(getStringAttribute(attributes, PHONE));
		user.setTown(getStringAttribute(attributes, TOWN));
		user.setPostalCode(getStringAttribute(attributes, POSTAL_CODE));
		user.setUuid(getStringAttribute(attributes, UUID));
		final String birthdate = getStringAttribute(attributes, BIRTHDATE);
		if (StringUtils.isNotEmpty(birthdate)) {
			user.setDayOfBirth(Date.valueOf(birthdate));
		}
	}

	private String getStringAttribute(final Map<String, Object> attributes, final String key) {
		if (attributes.containsKey(key)) {
			return (String) attributes.get(key);
		}
		return StringUtils.EMPTY;
	}
}
