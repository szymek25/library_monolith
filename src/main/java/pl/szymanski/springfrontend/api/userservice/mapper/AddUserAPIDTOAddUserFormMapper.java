package pl.szymanski.springfrontend.api.userservice.mapper;

import org.mapstruct.Mapper;
import pl.szymanski.springfrontend.api.userservice.dto.AddUserAPIDTO;
import pl.szymanski.springfrontend.forms.AddUserForm;

@Mapper(componentModel = "spring")
public interface AddUserAPIDTOAddUserFormMapper {

	AddUserAPIDTO map(AddUserForm form);
}
