package pl.szymanski.springfrontend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.szymanski.springfrontend.constants.ExceptionCodes;
import pl.szymanski.springfrontend.dao.DepartmentDao;
import pl.szymanski.springfrontend.dao.UserDao;
import pl.szymanski.springfrontend.dtos.DepartmentDTO;
import pl.szymanski.springfrontend.exceptions.DepartmentException;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.model.Department;
import pl.szymanski.springfrontend.model.User;
import pl.szymanski.springfrontend.service.DepartmentService;
import pl.szymanski.springfrontend.service.UserService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

  @Autowired
  private DepartmentDao departmentDao;

  @Autowired
  private UserDao userDao;

  @Autowired
  private UserService userService;

  @Override
  public void addNewDepartment(final DepartmentDTO departmentDTO) throws DepartmentException {
    if (departmentDao.existsByName(departmentDTO.getName())) {
      throw new DepartmentException(ExceptionCodes.DEPARTMENT_ARLEADY_EXSIST);
    }

    final Department department = new Department();
    convertDepartmentDTO(departmentDTO, department);
    departmentDao.save(department);
  }

  @Override
  public Page<Department> getAllPaginated(Pageable pageable) {
    return departmentDao.findAll(pageable);
  }

  @Override
  public Department getDepartmentById(final int id) {
    return departmentDao.findByDepartmentId(id);
  }

  @Override
  public void updateDepartment(final int id, final DepartmentDTO departmentDTO)
      throws DepartmentException {
    final Department department = getDepartmentById(id);
    if (department == null) {
      throw new DepartmentException(ExceptionCodes.DEPARTMENT_DOES_NOT_EXSIST);
    }

    if (!department.getName().equalsIgnoreCase(departmentDTO.getName()) && departmentDao
        .existsByName(departmentDTO.getName())) {
      throw new DepartmentException(ExceptionCodes.DEPARTMENT_ARLEADY_EXSIST);
    }

    updateDepartment(departmentDTO, department);
    departmentDao.save(department);
  }

  @Override
  public void deleteDepartment(final int id) {
    departmentDao.deleteById(id);
  }

  @Override
  public void addEmployeeToDepartment(final User user, final Department department) {
    user.setDepartment(department);
    userDao.save(user);
  }

  @Override
  public void removeEmployeeFromDepartment(final User user) {
    user.setDepartment(null);
    userDao.save(user);
  }

  @Override
  public boolean checkIfCurrentUserHasPermissionToBook(final BookEntry bookEntry) {
    final Department bookDepartment = bookEntry.getDepartment();
    final User currentUser = userService.getCurrentUser();
    final Department userDepartment = currentUser.getDepartment();
    if (bookDepartment != null && userDepartment != null) {
      return bookDepartment.getDepartmentId() == userDepartment.getDepartmentId();
    }
    return false;
  }

  protected void updateDepartment(final DepartmentDTO departmentDTO, final Department department) {
    department.setName(departmentDTO.getName());
    department.setTown(departmentDTO.getTown());
    department.setPostalCode(departmentDTO.getPostalCode());
    department.setAddressLine1(departmentDTO.getAddressLine1());
    department.setIpAddress(departmentDTO.getIpAddress());
  }

  private void convertDepartmentDTO(final DepartmentDTO departmentDTO,
      final Department department) {
    department.setName(departmentDTO.getName());
    department.setAddressLine1(departmentDTO.getAddressLine1());
    department.setPostalCode(departmentDTO.getPostalCode());
    department.setTown(departmentDTO.getTown());
    department.setIpAddress(departmentDTO.getIpAddress());
  }
}
