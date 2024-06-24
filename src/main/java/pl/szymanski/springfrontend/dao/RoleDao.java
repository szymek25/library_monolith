package pl.szymanski.springfrontend.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.szymanski.springfrontend.model.Role;

@Repository
public interface RoleDao extends CrudRepository<Role, Long> {

  Role findById(long id);

  Role findByName(String name);
}
