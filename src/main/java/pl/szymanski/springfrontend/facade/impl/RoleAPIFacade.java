package pl.szymanski.springfrontend.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.api.userservice.UserServiceApi;
import pl.szymanski.springfrontend.api.userservice.dto.RoleAPIDTO;
import pl.szymanski.springfrontend.api.userservice.mapper.RoleDTORoleAPIDTOMapper;
import pl.szymanski.springfrontend.dtos.RoleDTO;

import java.util.List;

@Component
@Primary
@ConditionalOnProperty(value = "library.user-service-enabled", havingValue = "true")
public class RoleAPIFacade extends RoleFacadeImpl {

	@Autowired
	private UserServiceApi userServiceApi;

	@Autowired
	private RoleDTORoleAPIDTOMapper mapper;
	@Override
	public List<RoleDTO> getAllRoles() {
		final List<RoleAPIDTO> allRoles = userServiceApi.getAllRoles();
		return mapper.map(allRoles);
	}
}
