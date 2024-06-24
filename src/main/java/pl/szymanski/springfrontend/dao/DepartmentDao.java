package pl.szymanski.springfrontend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import pl.szymanski.springfrontend.model.Department;

public interface DepartmentDao extends CrudRepository<Department, Integer> {

  boolean existsByName(String name);

  Page<Department> findAll(Pageable pageable);

  Department findByDepartmentId(int departmentId);

}
