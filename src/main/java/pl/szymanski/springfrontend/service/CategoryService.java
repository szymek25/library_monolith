package pl.szymanski.springfrontend.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.szymanski.springfrontend.dtos.CategoryDTO;
import pl.szymanski.springfrontend.exceptions.CategoryException;
import pl.szymanski.springfrontend.model.Category;

public interface CategoryService {

  boolean addNewCategory(CategoryDTO categoryDTO) throws CategoryException;

  List<Category> getAll();

  Page<Category> getAllPaginated(Pageable pageable);

  String deleteCategory(int id);

  Category getCategoryById(int id);

  boolean updateCategory(int id, CategoryDTO categoryDTO) throws CategoryException;
}
