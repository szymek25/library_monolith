package pl.szymanski.springfrontend.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.szymanski.springfrontend.dtos.UserDTO;
import pl.szymanski.springfrontend.exceptions.DuplicatedUserException;
import pl.szymanski.springfrontend.forms.AddUserForm;
import pl.szymanski.springfrontend.forms.EditUserForm;
import pl.szymanski.springfrontend.forms.RegisterForm;

public interface UserFacade {

  Page<UserDTO> getPaginatedUser(Pageable pageable);

  Page<UserDTO> getPaginatedLibraryCustomers(Pageable pageable);

  Page<UserDTO> getPaginatedLibraryEmployees(Pageable pageable);

  UserDTO getUserById(String id);

  boolean updateUser(String id, EditUserForm editUserForm);

  boolean updateUserPassword(String userId, String password);

  boolean addNewUser(AddUserForm userForm) throws DuplicatedUserException;

  boolean delete(String id);

  boolean registerUser(RegisterForm registerForm) throws DuplicatedUserException;

  boolean existsUserByEmail(String email);

  Integer getUserIdByEmail(String email);

  boolean isCurrentUserManager();

  boolean isLoggedIn();
}
