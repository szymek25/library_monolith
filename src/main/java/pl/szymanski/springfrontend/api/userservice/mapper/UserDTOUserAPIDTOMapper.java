package pl.szymanski.springfrontend.api.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.szymanski.springfrontend.api.userservice.dto.UserAPIDTO;
import pl.szymanski.springfrontend.dtos.UserDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDTOUserAPIDTOMapper  extends AbstractAPIResponseMapper<UserAPIDTO, UserDTO> {

	@Mapping(target = "keycloakRoleId", source = "roleId")
	@Mapping(target = "roleId", ignore = true)
	UserDTO map(UserAPIDTO userAPIDTO);

	List<UserDTO> map(List<UserAPIDTO> userAPIDTOS);
}
