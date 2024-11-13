package pl.szymanski.springfrontend.api.userservice.impl;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.szymanski.springfrontend.api.userservice.UserServiceApi;
import pl.szymanski.springfrontend.api.userservice.dto.PageDTO;
import pl.szymanski.springfrontend.api.userservice.dto.UserAPIDTO;
import pl.szymanski.springfrontend.api.userservice.dto.UserAPIResponseDTO;

import static pl.szymanski.springfrontend.constants.ApplicationConstants.UserService.ALL_USERS_ENDPOINT;
import static pl.szymanski.springfrontend.constants.ApplicationConstants.UserService.CURRENT_PAGE_PARAM;
import static pl.szymanski.springfrontend.constants.ApplicationConstants.UserService.LIBRARY_CUSTOMERS_ENDPOINT;
import static pl.szymanski.springfrontend.constants.ApplicationConstants.UserService.LIBRARY_EMPLOYEES_ENDPOINT;
import static pl.szymanski.springfrontend.constants.ApplicationConstants.UserService.PAGE_SIZE_PARAM;

@Service
public class UserServiceApiImpl implements UserServiceApi {

	private static final Logger LOG = LoggerFactory.getLogger(UserServiceApiImpl.class);
	@Autowired
	private RestTemplate restTemplate;

	@Value("${library.user-service-request-url}")
	private String userServiceUrl;

	@Override
	public UserAPIResponseDTO getLibraryCustomers(int currentPage, int pageSize) {
		String customersEndpoint = userServiceUrl + LIBRARY_CUSTOMERS_ENDPOINT;
		return fetchUsers(customersEndpoint, currentPage, pageSize);
	}

	@Override
	public UserAPIResponseDTO getLibraryEmployees(int currentPage, int pageSize) {
		String employeesEndpoint = userServiceUrl + LIBRARY_EMPLOYEES_ENDPOINT;
		return fetchUsers(employeesEndpoint, currentPage, pageSize);
	}

	@Override
	public UserAPIResponseDTO getAllUsers(int currentPage, int pageSize) {
		String allUsersEndpoint = userServiceUrl + ALL_USERS_ENDPOINT;
		return fetchUsers(allUsersEndpoint, currentPage, pageSize);
	}

	@Override
	public UserAPIDTO getUserById(String id) {
		final String endpoint = userServiceUrl + "/users/" + id;

		try {
			return restTemplate.getForEntity(
					endpoint, UserAPIDTO.class).getBody();
		} catch (Exception e) {
			LOG.error("Error while fetching user {} from user service", id, e);
			return new UserAPIDTO();
		}
	}

	private UserAPIResponseDTO fetchUsers(String endpoint, int currentPage, int pageSize) {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(endpoint)
				.queryParam(CURRENT_PAGE_PARAM, currentPage).queryParam(PAGE_SIZE_PARAM, pageSize);

		try {
			return restTemplate.getForEntity(
					builder.toUriString(), UserAPIResponseDTO.class).getBody();
		} catch (Exception e) {
			LOG.error("Error while fetching users from user service", e);
		}
		return createEmptyResults();
	}

	private UserAPIResponseDTO createEmptyResults() {
		UserAPIResponseDTO userAPIResponseDTO = new UserAPIResponseDTO();
		PageDTO pageDTO = new PageDTO();
		pageDTO.setNumber(0);
		pageDTO.setSize(1);
		pageDTO.setTotalPages(1);
		userAPIResponseDTO.setPage(pageDTO);
		userAPIResponseDTO.setContent(Lists.newArrayList());

		return userAPIResponseDTO;
	}
}
