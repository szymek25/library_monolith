package pl.szymanski.springfrontend.facade;


import java.util.List;
import pl.szymanski.springfrontend.dtos.RoleDTO;

public interface RoleFacade {

  List<RoleDTO> getAllRoles();
}
