package pl.szymanski.springfrontend.api.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.szymanski.springfrontend.api.userservice.dto.UserAPIDTO;
import pl.szymanski.springfrontend.dtos.UserDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDTOUserAPIDTOMapper  extends AbstractAPIResponseMapper<UserAPIDTO, UserDTO> {

	@Mapping(target = "roleId", ignore = true)
	@Mapping(target = "accountType", source = "role.description")
	@Mapping(target = "keycloakRoleId", source = "role.id")
	UserDTO map(UserAPIDTO userAPIDTO);

	List<UserDTO> map(List<UserAPIDTO> userAPIDTOS);
}
