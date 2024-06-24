package pl.szymanski.springfrontend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.szymanski.springfrontend.dtos.DepartmentDTO;
import pl.szymanski.springfrontend.exceptions.DepartmentException;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.model.Department;
import pl.szymanski.springfrontend.model.User;

public interface DepartmentService {

  void addNewDepartment(DepartmentDTO departmentDTO) throws DepartmentException;

  Page<Department> getAllPaginated(Pageable pageable);

  Department getDepartmentById(int id);

  void updateDepartment(int id, DepartmentDTO departmentDTO) throws DepartmentException;

  void deleteDepartment(int id);

  void addEmployeeToDepartment(User user, Department department);

  void removeEmployeeFromDepartment(User user);

  boolean checkIfCurrentUserHasPermissionToBook(BookEntry bookEntry);
}
