package pl.szymanski.springfrontend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.szymanski.springfrontend.model.Category;

@Repository
public interface CategoryDao extends CrudRepository<Category, Integer> {

  boolean existsByName(String name);

  Page<Category> findAll(Pageable pageable);

}
