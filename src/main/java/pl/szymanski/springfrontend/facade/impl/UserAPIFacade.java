package pl.szymanski.springfrontend.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.api.userservice.UserServiceApi;
import pl.szymanski.springfrontend.api.userservice.converter.APIResponseConverter;
import pl.szymanski.springfrontend.api.userservice.dto.AddUserAPIDTO;
import pl.szymanski.springfrontend.api.userservice.dto.RegisterUserAPIDTO;
import pl.szymanski.springfrontend.api.userservice.dto.UserAPIResponseDTO;
import pl.szymanski.springfrontend.api.userservice.kafka.KafkaMessageService;
import pl.szymanski.springfrontend.api.userservice.mapper.AddUserAPIDTOAddUserFormMapper;
import pl.szymanski.springfrontend.api.userservice.mapper.RegisterUserAPIDTORegisterFormMapper;
import pl.szymanski.springfrontend.api.userservice.mapper.UpdateUserEventEditUserFormMapper;
import pl.szymanski.springfrontend.api.userservice.mapper.UserDTOUserAPIDTOMapper;
import pl.szymanski.springfrontend.avro.RemoveUserEvent;
import pl.szymanski.springfrontend.avro.UpdatePasswordEvent;
import pl.szymanski.springfrontend.avro.UpdateUserEvent;
import pl.szymanski.springfrontend.dtos.UserDTO;
import pl.szymanski.springfrontend.exceptions.DuplicatedUserException;
import pl.szymanski.springfrontend.forms.AddUserForm;
import pl.szymanski.springfrontend.forms.EditUserForm;
import pl.szymanski.springfrontend.forms.RegisterForm;

@Component
@Primary
@ConditionalOnProperty(value = "library.user-service-enabled", havingValue = "true")
public class UserAPIFacade extends UserFacadeImpl {

	@Autowired
	private UserServiceApi userServiceApi;

	@Autowired
	private APIResponseConverter<UserAPIResponseDTO, UserDTO> userAPIDTOResponseConverter;

	@Autowired
	private UserDTOUserAPIDTOMapper userDTOUserAPIDTOMapper;

	@Autowired
	private UpdateUserEventEditUserFormMapper updateUserEventEditUserFormMapper;

	@Autowired
	private KafkaMessageService kafkaMessageService;

	@Autowired
	private AddUserAPIDTOAddUserFormMapper addUserAPIDTOAddUserFormMapper;

	@Autowired
	private RegisterUserAPIDTORegisterFormMapper registerUserAPIDTORegisterFormMapper;

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
	public UserDTO getUserById(final int id) {
		return userDTOUserAPIDTOMapper.map(userServiceApi.getUserById(String.valueOf(id)));
	}

	@Override
	public boolean updateUser(final int id, final EditUserForm editUserForm) {
		final UpdateUserEvent updateUserEvent = updateUserEventEditUserFormMapper.map(editUserForm, id);
		kafkaMessageService.sendUserUpdateMessage(updateUserEvent);
		return true;
	}

	@Override
	public boolean delete(int id) {
		kafkaMessageService.sendUserDeleteMessage(new RemoveUserEvent(id));
		return true;
	}

	@Override
	public boolean updateUserPassword(int userId, String password) {
		kafkaMessageService.sendUpdatePasswordMessage(new UpdatePasswordEvent(userId, password));
		return true;
	}

	@Override
	public boolean addNewUser(AddUserForm addUserForm) throws DuplicatedUserException {
		final AddUserAPIDTO addUserAPIDTO = addUserAPIDTOAddUserFormMapper.map(addUserForm);

		return userServiceApi.addUser(addUserAPIDTO);
	}

	@Override
	public boolean registerUser(RegisterForm registerForm) throws DuplicatedUserException {
		final RegisterUserAPIDTO registerUserAPIDTO = registerUserAPIDTORegisterFormMapper.map(registerForm);

		return userServiceApi.registerUser(registerUserAPIDTO);
	}

	@Override
	public boolean existsUserByEmail(String email) {
		return userServiceApi.getUserByEmail(email) != null;
	}
}
