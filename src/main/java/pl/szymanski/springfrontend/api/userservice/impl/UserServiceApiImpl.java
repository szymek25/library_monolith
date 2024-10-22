package pl.szymanski.springfrontend.api.userservice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.szymanski.springfrontend.api.userservice.dto.UserAPIResponseDTO;
import pl.szymanski.springfrontend.api.userservice.UserServiceApi;
import pl.szymanski.springfrontend.constants.ApplicationConstants;

import static pl.szymanski.springfrontend.constants.ApplicationConstants.UserService.CURRENT_PAGE_PARAM;
import static pl.szymanski.springfrontend.constants.ApplicationConstants.UserService.LIBRARY_CUSTOMERS_ENDPOINT;
import static pl.szymanski.springfrontend.constants.ApplicationConstants.UserService.PAGE_SIZE_PARAM;

@Service
public class UserServiceApiImpl implements UserServiceApi {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${library.user-service-request-url}")
	private String userServiceUrl;

	@Override
	public UserAPIResponseDTO getLibraryCustomers(int currentPage, int pageSize) {
		String endSessionEndpoint = userServiceUrl + LIBRARY_CUSTOMERS_ENDPOINT;
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(endSessionEndpoint)
				.queryParam(CURRENT_PAGE_PARAM, currentPage).queryParam(PAGE_SIZE_PARAM, pageSize);

		return restTemplate.getForEntity(
				builder.toUriString(), UserAPIResponseDTO.class).getBody();
	}
}
