package pl.szymanski.springfrontend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.szymanski.springfrontend.oauth2.KeycloakLogoutHandler;
import pl.szymanski.springfrontend.service.KeyCloakService;

@Service
public class KeyCloakServiceImpl implements KeyCloakService {

	private static final Logger logger = LoggerFactory.getLogger(KeyCloakServiceImpl.class);
	private static final String LOGOUT_URI = "/protocol/openid-connect/logout";
	@Autowired
	private RestTemplate restTemplate;
	@Value("${keycloak.baseUrl}")
	private String baseUrl;

	@Override
	public void logoutFromKeyCloak(OidcUser user) {
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

		SecurityContextHolder.clearContext();
	}

	@Override
	public void logoutFromKeyCloak() {
		OidcUser principal = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logoutFromKeyCloak(principal);
	}
}
