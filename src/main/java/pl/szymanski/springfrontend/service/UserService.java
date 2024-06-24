package pl.szymanski.springfrontend.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.szymanski.springfrontend.dtos.UserDTO;
import pl.szymanski.springfrontend.model.User;

public interface UserService {

  boolean addNewUser(UserDTO userDTO);

  boolean existsUserByEmail(String email);

  List<User> findAll();

  boolean delete(int id);

  boolean registerUser(UserDTO userDTO);

  Page<User> findAllPaginated(Pageable pageable);

  Page<User> findAllLibraryCustomers(Pageable pageable);

  Page<User> findAllLibraryEmployees(Pageable pageable);

  User getUserById(int id);

  boolean updateUser(int id, UserDTO updateUserDTO);

  boolean updateUserPassword(int userId, String password);

  User getUserByEmail(String email);

  boolean isCurrentUserManager();

  User getCurrentUser();
}
