package pl.szymanski.springfrontend.api.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szymanski.springfrontend.api.userservice.converter.APIResponseConverter;
import pl.szymanski.springfrontend.api.userservice.dto.UserAPIResponseDTO;
import pl.szymanski.springfrontend.api.userservice.mapper.UserDTOUserAPIDTOMapper;
import pl.szymanski.springfrontend.dtos.UserDTO;

@Configuration
public class UserApiConfig {

	@Bean
	public APIResponseConverter<UserAPIResponseDTO, UserDTO> userAPIDTOResponseConverter(UserDTOUserAPIDTOMapper userDTOUserAPIDTOMapper) {
		return new APIResponseConverter<>(userDTOUserAPIDTOMapper);
	}
}
