package pl.szymanski.springfrontend.service;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import pl.szymanski.springfrontend.model.User;

public interface KeyCloakService {

	void logoutFromKeyCloak(OidcUser user);

	void logoutFromKeyCloak();

	void updateUser(User user);
}
