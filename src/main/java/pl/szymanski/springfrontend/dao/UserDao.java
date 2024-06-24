package pl.szymanski.springfrontend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.szymanski.springfrontend.model.Role;
import pl.szymanski.springfrontend.model.User;

@Repository
public interface UserDao extends CrudRepository<User, Integer> {

  User findByEmail(String email);

  Page<User> findAll(Pageable pageable);

  Page<User> findByRole(Pageable pageable, Role role);

  User findById(int id);

  boolean existsByEmail(String email);
}
