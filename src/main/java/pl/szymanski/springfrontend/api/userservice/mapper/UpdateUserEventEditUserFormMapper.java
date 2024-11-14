package pl.szymanski.springfrontend.api.userservice.mapper;

import org.mapstruct.Mapper;
import pl.szymanski.springfrontend.api.userservice.events.UpdateUserEvent;
import pl.szymanski.springfrontend.forms.EditUserForm;

@Mapper(componentModel = "spring")
public interface  UpdateUserEventEditUserFormMapper {

	UpdateUserEvent map(EditUserForm form);
}
