package pl.szymanski.springfrontend.service;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface KeyCloakService {

	void logoutFromKeyCloak(OidcUser user);

	void logoutFromKeyCloak();
}
