package pl.szymanski.springfrontend.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.io.IOException;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

	@Autowired
	private final OAuth2AuthorizedClientService clientService;

	public RestTemplateInterceptor(OAuth2AuthorizedClientService clientService) {
		this.clientService = clientService;
	}


	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
		final OAuth2AuthorizedClient oAuth2AuthorizedClient = clientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
		request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + oAuth2AuthorizedClient.getAccessToken().getTokenValue());

		return execution.execute(request, body);

	}
}
