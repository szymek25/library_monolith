package pl.szymanski.springfrontend.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.api.userservice.UserServiceApi;
import pl.szymanski.springfrontend.api.userservice.converter.APIResponseConverter;
import pl.szymanski.springfrontend.api.userservice.dto.UserAPIResponseDTO;
import pl.szymanski.springfrontend.api.userservice.mapper.UserDTOUserAPIDTOMapper;
import pl.szymanski.springfrontend.dtos.UserDTO;

@Component
@Primary
@ConditionalOnProperty(value = "library.user-service-enabled", havingValue = "true")
public class UserAPIFacade extends UserFacadeImpl {

	@Autowired
	private UserServiceApi userServiceApi;


	@Autowired
	private APIResponseConverter<UserAPIResponseDTO, UserDTO> userAPIDTOResponseConverter;

	@Autowired private UserDTOUserAPIDTOMapper userDTOUserAPIDTOMapper;

	@Override
	public Page<UserDTO> getPaginatedLibraryCustomers(Pageable pageable) {
		final UserAPIResponseDTO libraryCustomers = userServiceApi.getLibraryCustomers(pageable.getPageNumber(), pageable.getPageSize());
		return userAPIDTOResponseConverter.convertToDTO(libraryCustomers);
	}

	@Override
	public Page<UserDTO> getPaginatedLibraryEmployees(Pageable pageable) {
		final UserAPIResponseDTO libraryEmployees = userServiceApi.getLibraryEmployees(pageable.getPageNumber(), pageable.getPageSize());
		return userAPIDTOResponseConverter.convertToDTO(libraryEmployees);
	}

	@Override
	public Page<UserDTO> getPaginatedUser(Pageable pageable) {
		final UserAPIResponseDTO allUsers = userServiceApi.getAllUsers(pageable.getPageNumber(), pageable.getPageSize());
		return userAPIDTOResponseConverter.convertToDTO(allUsers);
	}

	@Override
	public UserDTO getUserById(final String id) {
		return userDTOUserAPIDTOMapper.map(userServiceApi.getUserById(id));
	}
}
