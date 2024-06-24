package pl.szymanski.springfrontend.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.szymanski.springfrontend.dtos.DepartmentDTO;
import pl.szymanski.springfrontend.exceptions.DepartmentException;
import pl.szymanski.springfrontend.forms.DepartmentForm;

public interface DepartmentFacade {

  void addNewDepartment(DepartmentForm form) throws DepartmentException;

  Page<DepartmentDTO> getPaginatedDepartments(PageRequest pageRequest);

  DepartmentDTO getDepartmentById(int id);

  void updateDepartment(int id, DepartmentForm form) throws DepartmentException;

  void deleteDepartment(int id);

  void addEmployeeToDepartment(int employeeId, int departmentId) throws DepartmentException;

  void removeEmployeeFromDepartment(int employeeId) throws DepartmentException;
}
