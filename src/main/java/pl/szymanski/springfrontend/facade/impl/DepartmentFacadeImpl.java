package pl.szymanski.springfrontend.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.dtos.DepartmentDTO;
import pl.szymanski.springfrontend.exceptions.DepartmentException;
import pl.szymanski.springfrontend.facade.DepartmentFacade;
import pl.szymanski.springfrontend.forms.DepartmentForm;
import pl.szymanski.springfrontend.model.Department;
import pl.szymanski.springfrontend.model.User;
import pl.szymanski.springfrontend.service.DepartmentService;
import pl.szymanski.springfrontend.service.UserService;

@Component
public class DepartmentFacadeImpl implements DepartmentFacade {

  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private UserService userService;

  @Override
  public void addNewDepartment(final DepartmentForm form) throws DepartmentException {
    final DepartmentDTO departmentDTO = createDepartmentDTO(form);

    departmentService.addNewDepartment(departmentDTO);
  }

  @Override
  public Page<DepartmentDTO> getPaginatedDepartments(final PageRequest pageRequest) {
    return convertToPageDepartmentDTO(departmentService.getAllPaginated(pageRequest));

  }

  @Override
  public DepartmentDTO getDepartmentById(final int id) {
    final Department department = departmentService.getDepartmentById(id);
    if (department != null) {
      return convertToDepartmentDto(department);
    }
    return null;
  }

  @Override
  public void updateDepartment(int id, DepartmentForm form) throws DepartmentException {
    final DepartmentDTO departmentDTO = new DepartmentDTO();
    departmentDTO.setName(form.getName());
    departmentDTO.setTown(form.getTown());
    departmentDTO.setAddressLine1(form.getAddressLine1());
    departmentDTO.setPostalCode(form.getPostalCode());
    departmentDTO.setIpAddress(form.getIpAddress());

    departmentService.updateDepartment(id, departmentDTO);
  }

  @Override
  public void deleteDepartment(final int id) {
    departmentService.deleteDepartment(id);
  }

  @Override
  public void addEmployeeToDepartment(final int employeeId, final int departmentId)
      throws DepartmentException {
    final User user = userService.getUserById(employeeId);
    final Department department = departmentService.getDepartmentById(departmentId);
    if (user == null || department == null) {
      throw new DepartmentException();
    }

    departmentService.addEmployeeToDepartment(user, department);
  }

  @Override
  public void removeEmployeeFromDepartment(final int employeeId) throws DepartmentException {
    final User user = userService.getUserById(employeeId);
    if (user == null) {
      throw new DepartmentException();
    }

    departmentService.removeEmployeeFromDepartment(user);
  }

  protected Page<DepartmentDTO> convertToPageDepartmentDTO(final Page<Department> allPaginated) {
    return allPaginated.map(this::convertToDepartmentDto);
  }

  protected DepartmentDTO convertToDepartmentDto(final Department department) {
    return new DepartmentDTO(department);
  }

  protected DepartmentDTO createDepartmentDTO(final DepartmentForm form) {
    final DepartmentDTO departmentDTO = new DepartmentDTO();
    departmentDTO.setName(form.getName());
    departmentDTO.setAddressLine1(form.getAddressLine1());
    departmentDTO.setPostalCode(form.getPostalCode());
    departmentDTO.setTown(form.getTown());
    departmentDTO.setIpAddress(form.getIpAddress());

    return departmentDTO;
  }
}
