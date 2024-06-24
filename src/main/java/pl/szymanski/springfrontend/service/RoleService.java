package pl.szymanski.springfrontend.service;


import java.util.List;
import pl.szymanski.springfrontend.model.Role;

public interface RoleService {

  List<Role> getAll();

  Role getById(long id);

  Role getByName(String name);
}
