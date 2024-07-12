package pl.szymanski.springfrontend.oauth2;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.util.MultiValueMap;


public class CustomOAuth2AuthorizationCodeGrantRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

	private static final String CLIENT_ID = "client_id";
	private OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

	public final String clientId;

	public CustomOAuth2AuthorizationCodeGrantRequestEntityConverter(String clientId) {
		this.clientId = clientId;
		defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
	}

	@Override
	public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
		RequestEntity<?> entity = defaultConverter.convert(req);
		MultiValueMap<String, String> params = (MultiValueMap<String,String>) entity.getBody();
		params.add(CLIENT_ID, clientId);
		return new RequestEntity<>(params, entity.getHeaders(), entity.getMethod(), entity.getUrl());
	}
}
