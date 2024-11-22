package pl.szymanski.springfrontend.api.userservice;

import pl.szymanski.springfrontend.api.userservice.dto.RoleAPIDTO;
import pl.szymanski.springfrontend.api.userservice.dto.UserAPIDTO;
import pl.szymanski.springfrontend.api.userservice.dto.UserAPIResponseDTO;

import java.util.List;

public interface UserServiceApi {

	UserAPIResponseDTO getLibraryCustomers(int currentPage, int pageSize);

	UserAPIResponseDTO getLibraryEmployees(int currentPage, int pageSize);

	UserAPIResponseDTO getAllUsers(int currentPage, int pageSize);

	UserAPIDTO getUserById(String id);

	List<RoleAPIDTO> getAllRoles();

}
