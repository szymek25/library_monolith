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
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.dtos.UserRepresentationDTO;
import pl.szymanski.springfrontend.model.User;
import pl.szymanski.springfrontend.service.KeyCloakService;

import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeyCloakServiceImpl implements KeyCloakService {

	private static final Logger logger = LoggerFactory.getLogger(KeyCloakServiceImpl.class);
	private static final String LOGOUT_URI = "/protocol/openid-connect/logout";
	@Autowired
	private RestTemplate restTemplate;
	@Value("${keycloak.baseUrl}")
	private String baseUrl;

	@Value("${keycloak.usersEndpoint}")
	private String usersEndpoint;

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

	@Override
	public void updateUser(User user) {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(usersEndpoint)
				.pathSegment(user.getUuid());


		restTemplate.put(builder.toUriString(), mapUser(user));

	}

	private UserRepresentationDTO mapUser(User user) {
		UserRepresentationDTO userRepresentationDTO = new UserRepresentationDTO();
		userRepresentationDTO.setUsername(user.getEmail());
		userRepresentationDTO.setEmail(user.getEmail());
		userRepresentationDTO.setFirstName(user.getName());
		userRepresentationDTO.setLastName(user.getLastName());

		Map<String, List<String>> attributes = new HashMap<>();
		attributes.put(ApplicationConstants.KeyCloak.PHONE, Collections.singletonList(user.getPhone()));
		attributes.put(ApplicationConstants.KeyCloak.ADDRESS_LINE_1, Collections.singletonList(user.getAddressLine1()));
		attributes.put(ApplicationConstants.KeyCloak.TOWN, Collections.singletonList(user.getTown()));
		attributes.put(ApplicationConstants.KeyCloak.POSTAL_CODE, Collections.singletonList(user.getPostalCode()));
		Date dayOfBirth = user.getDayOfBirth();
		if (dayOfBirth != null) {
			attributes.put(ApplicationConstants.KeyCloak.BIRTHDATE, Collections.singletonList(dayOfBirth.toString()));
		}

		//Role

		userRepresentationDTO.setAttributes(attributes);
		return userRepresentationDTO;
	}
}
