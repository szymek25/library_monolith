package pl.szymanski.springfrontend.facade.impl;

import java.sql.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.dtos.UserDTO;
import pl.szymanski.springfrontend.exceptions.DuplicatedUserException;
import pl.szymanski.springfrontend.facade.UserFacade;
import pl.szymanski.springfrontend.forms.AddUserForm;
import pl.szymanski.springfrontend.forms.EditUserForm;
import pl.szymanski.springfrontend.forms.RegisterForm;
import pl.szymanski.springfrontend.model.User;
import pl.szymanski.springfrontend.service.UserService;

@Component
public class UserFacadeImpl implements UserFacade {

  private static final Logger LOG = LoggerFactory.getLogger(UserFacadeImpl.class);

  @Autowired
  private UserService userService;

  @Override
  public Page<UserDTO> getPaginatedUser(Pageable pageable) {
    Page<User> allUsers = userService.findAllPaginated(pageable);
    return allUsers != null ? allUsers.map(UserDTO::new) : null;
  }

  @Override
  public Page<UserDTO> getPaginatedLibraryCustomers(Pageable pageable) {
    final Page<User> allLibraryCustomers = userService.findAllLibraryCustomers(pageable);
    return allLibraryCustomers != null ? allLibraryCustomers.map(UserDTO::new) : null;
  }

  @Override
  public Page<UserDTO> getPaginatedLibraryEmployees(Pageable pageable) {
    final Page<User> allLibraryCustomers = userService.findAllLibraryEmployees(pageable);
    return allLibraryCustomers != null ? allLibraryCustomers.map(UserDTO::new) : null;
  }

  @Override
  public UserDTO getUserById(int id) {
    User user = userService.getUserById(id);
    return user != null ? new UserDTO(user) : null;
  }

  @Override
  public boolean updateUser(final int id, final EditUserForm editUserForm) {
    final UserDTO userDTO = new UserDTO();
    userDTO.setEmail(editUserForm.getEmail());
    userDTO.setName(editUserForm.getName());
    userDTO.setLastName(editUserForm.getLastName());
    userDTO.setDayOfBirth(Date.valueOf(editUserForm.getDayOfBirth()));
    userDTO.setRoleId(editUserForm.getRoleId());
    userDTO.setAddressLine1(editUserForm.getAddressLine1());
    userDTO.setTown(editUserForm.getTown());
    userDTO.setPostalCode(editUserForm.getPostalCode());
    userDTO.setPhone(editUserForm.getPhone());
    return userService.updateUser(id, userDTO);
  }

  @Override
  public boolean updateUserPassword(int userId, String password) {
    return userService.updateUserPassword(userId, password);
  }

  @Override
  public boolean addNewUser(AddUserForm addUserForm) throws DuplicatedUserException {
    boolean userAlreadyExists = existsUserByEmail(addUserForm.getEmail());
    if (userAlreadyExists) {
      throw new DuplicatedUserException("User with email " + addUserForm.getEmail() + " already exists");
    }
    final UserDTO userDTO = new UserDTO();
    userDTO.setEmail(addUserForm.getEmail());
    try {
      userDTO.setRoleId(addUserForm.getRoleId());
    } catch (NumberFormatException e) {
      LOG.warn("Role id wasn`t a number");
    }
    userDTO.setPassword(addUserForm.getPassword());
    userDTO.setName(addUserForm.getName());
    userDTO.setLastName(addUserForm.getLastName());
    userDTO.setDayOfBirth(Date.valueOf(addUserForm.getDayOfBirth()));
    userDTO.setAddressLine1(addUserForm.getAddressLine1());
    userDTO.setTown(addUserForm.getTown());
    userDTO.setPostalCode(addUserForm.getPostalCode());
    userDTO.setPhone(addUserForm.getPhone());
    return userService.addNewUser(userDTO);
  }

  @Override
  public boolean delete(int id) {
    return userService.delete(id);
  }

  @Override
  public boolean registerUser(RegisterForm registerForm) throws DuplicatedUserException {
	  if (existsUserByEmail(registerForm.getEmail())) {
		  throw new DuplicatedUserException("User with email " + registerForm.getEmail() + " already exists");
	  }
	  final UserDTO userDTO = new UserDTO();
	  userDTO.setEmail(registerForm.getEmail());
	  userDTO.setPassword(registerForm.getPassword());
	  userDTO.setName(registerForm.getName());
	  userDTO.setLastName(registerForm.getLastName());
	  userDTO.setDayOfBirth(Date.valueOf(registerForm.getDayOfBirth()));
	  userDTO.setAddressLine1(registerForm.getAddressLine1());
	  userDTO.setTown(registerForm.getTown());
	  userDTO.setPostalCode(registerForm.getPostalCode());
	  userDTO.setPhone(registerForm.getPhone());

	  return userService.registerUser(userDTO);
  }

  @Override
  public boolean existsUserByEmail(String email) {
    return userService.existsUserByEmail(email);
  }

  @Override
  public Integer getUserIdByEmail(String email) {
    User user = userService.getUserByEmail(email);
    if (user != null) {
      return user.getId();
    }
    return null;
  }

  @Override
  public boolean isCurrentUserManager() {
    return userService.isCurrentUserManager();
  }

  @Override
  public boolean isLoggedIn() {
    User currentUser = userService.getCurrentUser();
    return currentUser != null;
  }
}
