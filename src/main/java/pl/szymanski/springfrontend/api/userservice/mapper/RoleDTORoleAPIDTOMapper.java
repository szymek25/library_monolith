package pl.szymanski.springfrontend.api.userservice.mapper;

import org.mapstruct.Mapper;
import pl.szymanski.springfrontend.api.userservice.dto.RoleAPIDTO;
import pl.szymanski.springfrontend.dtos.RoleDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleDTORoleAPIDTOMapper {

	RoleDTO map(RoleAPIDTO roleAPIDTO);
	List<RoleDTO> map(List<RoleAPIDTO> roleAPIDTOS);
}
