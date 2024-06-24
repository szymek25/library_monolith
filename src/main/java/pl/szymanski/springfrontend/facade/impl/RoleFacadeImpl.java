package pl.szymanski.springfrontend.facade.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.dtos.RoleDTO;
import pl.szymanski.springfrontend.facade.RoleFacade;
import pl.szymanski.springfrontend.model.Role;
import pl.szymanski.springfrontend.service.RoleService;

@Component
public class RoleFacadeImpl implements RoleFacade {

  @Autowired
  private RoleService roleService;

  @Override
  public List<RoleDTO> getAllRoles() {
    List<Role> roles = roleService.getAll();
    if (roles != null) {
      return roles.stream().map(RoleDTO::new).collect(Collectors.toList());
    }
    return null;
  }
}
