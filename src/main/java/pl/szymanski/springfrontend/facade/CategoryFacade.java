package pl.szymanski.springfrontend.facade;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.szymanski.springfrontend.dtos.CategoryDTO;
import pl.szymanski.springfrontend.exceptions.CategoryException;
import pl.szymanski.springfrontend.forms.CategoryForm;

public interface CategoryFacade {

  Page<CategoryDTO> getCategoriesForListing(Pageable pageable);

  List<CategoryDTO> getAllCategories();

  boolean addNewCategory(CategoryForm categoryForm) throws CategoryException;

  String deleteCategory(int id);

  CategoryDTO getCategoryById(int id);

  boolean updateCategory(int id,  CategoryForm form) throws CategoryException;

}
