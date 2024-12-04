package pl.szymanski.springfrontend.api.userservice.mapper;

import org.mapstruct.Mapper;
import pl.szymanski.springfrontend.api.userservice.dto.RegisterUserAPIDTO;
import pl.szymanski.springfrontend.forms.RegisterForm;

@Mapper(componentModel = "spring")
public interface RegisterUserAPIDTORegisterFormMapper {

	RegisterUserAPIDTO map(RegisterForm form);
}
