package pl.szymanski.springfrontend.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.dao.UserDao;
import pl.szymanski.springfrontend.dtos.UserDTO;
import pl.szymanski.springfrontend.model.Role;
import pl.szymanski.springfrontend.model.User;
import pl.szymanski.springfrontend.service.RoleService;
import pl.szymanski.springfrontend.service.UserService;


@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

  @Autowired
  private UserDao userDao;

  @Autowired
  RoleService roleService;

  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    User user = userDao.findByEmail(userId);
    if (user == null) {
      throw new UsernameNotFoundException("Invalid username or password.");
    }
    return new org.springframework.security.core.userdetails.User(user.getEmail(),
        user.getPassword(),
        getAuthority(user));
  }

  private List<SimpleGrantedAuthority> getAuthority(User user) {
    return Arrays.asList(new SimpleGrantedAuthority(user.getRole().getName()));
  }

  public List<User> findAll() {
    List<User> list = new ArrayList<>();
    userDao.findAll().iterator().forEachRemaining(list::add);
    return list;
  }

  @Override
  public boolean delete(int id) {
    userDao.deleteById(id);
    return true;
  }

  @Override
  public boolean registerUser(UserDTO userDTO) {
    if (userDao.existsByEmail(userDTO.getEmail())) {
      return false;
    }
    User user = new User();
    try {
      convertUserDTO(userDTO, user);
      setUserRole(user);
      userDao.save(user);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  @Override
  public Page<User> findAllPaginated(Pageable pageable) {
    return userDao.findAll(pageable);
  }

  @Override
  public Page<User> findAllLibraryCustomers(Pageable pageable) {
    final Role userRole = roleService.getByName(ApplicationConstants.USER_ROLE_NAME);
    return userDao.findByRole(pageable, userRole);
  }

  @Override
  public Page<User> findAllLibraryEmployees(Pageable pageable) {
    final Role userRole = roleService.getByName(ApplicationConstants.EMPLOYEE_ROLE_NAME);
    return userDao.findByRole(pageable, userRole);
  }

  @Override
  public User getUserById(int id) {
    return userDao.findById(id);
  }

  @Override
  public boolean updateUser(final int id, final UserDTO updateUserDto) {
    final User user = getUserById(id);
    if (user == null) {
      return false;
    }

    try {
      updateUser(updateUserDto, user);
      userDao.save(user);
    } catch (Exception e) {
      return false;
    }

    return true;
  }

  @Override
  public boolean updateUserPassword(int userId, String password) {
    User user = getUserById(userId);
    if (user == null) {
      return false;
    }
    user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
    userDao.save(user);

    return true;
  }

  @Override
  public User getUserByEmail(String email) {
    return userDao.findByEmail(email);
  }

  @Override
  public boolean isCurrentUserManager() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

    return authorities.stream()
        .anyMatch(authority -> ApplicationConstants.MANAGER_ROLE_NAME
            .equals(((GrantedAuthority) authority).getAuthority()));
  }

  @Override
  public User getCurrentUser() {
    final String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return userDao.findByEmail(username);
  }

  @Override
  public void save(User user) {
    userDao.save(user);
  }

  private void updateUser(UserDTO updateUserDto, User user) throws Exception {
    user.setName(updateUserDto.getName());
    user.setLastName(updateUserDto.getLastName());
    user.setDayOfBirth(updateUserDto.getDayOfBirth());
    user.setAddressLine1(updateUserDto.getAddressLine1());
    user.setTown(updateUserDto.getTown());
    user.setPostalCode(updateUserDto.getPostalCode());
    user.setPhone(updateUserDto.getPhone());
    setRole(updateUserDto, user);
  }

  @Override
  public boolean addNewUser(UserDTO userDTO) {
    if (userDao.existsByEmail(userDTO.getEmail())) {
      return false;
    }
    User user = new User();
    try {
      convertUserDTO(userDTO, user);
      setRole(userDTO, user);
      userDao.save(user);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  @Override
  public boolean existsUserByEmail(String email) {
    return userDao.existsByEmail(email);
  }

  private void convertUserDTO(UserDTO userDTO, User user) {
    String password = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
    user.setPassword(password);
    user.setEmail(userDTO.getEmail());
    user.setDayOfBirth(userDTO.getDayOfBirth());
    user.setAddressLine1(userDTO.getAddressLine1());
    user.setLastName(userDTO.getLastName());
    user.setName(userDTO.getName());
    user.setPhone(userDTO.getPhone());
    user.setPostalCode(userDTO.getPostalCode());
    user.setTown(userDTO.getTown());
  }

  private void setRole(UserDTO userDTO, User user) throws Exception {
    Role role = roleService.getById(userDTO.getRoleId());
    if (role == null) {
      throw new Exception("Didn`t find user role with id: " + userDTO.getRoleId());
    }
    user.setRole(role);
  }

  private void setUserRole(User user) throws Exception {
    Role role = roleService.getByName(ApplicationConstants.USER_ROLE_NAME);
    if (role == null) {
      throw new Exception(
          "Didn`t find user role with name: " + ApplicationConstants.USER_ROLE_NAME);
    }
    user.setRole(role);
  }
}
