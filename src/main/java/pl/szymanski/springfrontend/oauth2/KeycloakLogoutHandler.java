package pl.szymanski.springfrontend.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class KeycloakLogoutHandler implements LogoutHandler {

	private static final Logger logger = LoggerFactory.getLogger(KeycloakLogoutHandler.class);
	private static final String LOGOUT_URI = "/protocol/openid-connect/logout";
	@Autowired
	private final RestTemplate restTemplate;
	@Value("${keycloak.baseUrl}")
	private String baseUrl;

	public KeycloakLogoutHandler(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response,
					   Authentication auth) {
		logoutFromKeycloak((OidcUser) auth.getPrincipal());
	}

	private void logoutFromKeycloak(OidcUser user) {
		String endSessionEndpoint = baseUrl + LOGOUT_URI;
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(endSessionEndpoint)
				.queryParam("id_token_hint", user.getIdToken().getTokenValue());

		ResponseEntity<String> logoutResponse = restTemplate.getForEntity(
				builder.toUriString(), String.class);
		if (logoutResponse.getStatusCode().is2xxSuccessful()) {
			logger.info("Successfulley logged out from Keycloak");
		} else {
			logger.error("Could not propagate logout to Keycloak");
		}
	}
}
