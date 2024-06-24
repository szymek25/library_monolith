package pl.szymanski.springfrontend.facade.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.dtos.CategoryDTO;
import pl.szymanski.springfrontend.exceptions.CategoryException;
import pl.szymanski.springfrontend.facade.CategoryFacade;
import pl.szymanski.springfrontend.forms.CategoryForm;
import pl.szymanski.springfrontend.model.Book;
import pl.szymanski.springfrontend.model.Category;
import pl.szymanski.springfrontend.service.BookService;
import pl.szymanski.springfrontend.service.CategoryService;

@Component
public class CategoryFacadeImpl implements CategoryFacade {

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private BookService bookService;

  @Override
  public Page<CategoryDTO> getCategoriesForListing(final Pageable pageable) {
    Page<Category> categories = categoryService.getAllPaginated(pageable);

    return categories.map(this::convertToCategoryDTO);
  }

  @Override
  public List<CategoryDTO> getAllCategories() {
    List<Category> categories = categoryService.getAll();
    if (categories != null) {
      return categories.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }
    return null;
  }

  @Override
  public boolean addNewCategory(CategoryForm categoryForm) throws CategoryException {
    final CategoryDTO categoryDTO = new CategoryDTO();
    categoryDTO.setName(categoryForm.getName());
    categoryDTO.setDaysOfRent(categoryForm.getDaysOfRent());
    categoryDTO.setContinuationPossible(categoryForm.getContinuationPossible());

    return categoryService.addNewCategory(categoryDTO);
  }

  @Override
  public String deleteCategory(int id) {
    return categoryService.deleteCategory(id);
  }

  @Override
  public CategoryDTO getCategoryById(int id) {
    Category category = categoryService.getCategoryById(id);
    return category != null ? new CategoryDTO(category) : null;
  }

  @Override
  public boolean updateCategory(int id, CategoryForm form) throws CategoryException {
    final CategoryDTO categoryDTO = new CategoryDTO();
    categoryDTO.setName(form.getName());
    categoryDTO.setDaysOfRent(form.getDaysOfRent());
    categoryDTO.setContinuationPossible(form.getContinuationPossible());

    return categoryService.updateCategory(id, categoryDTO);
  }

  private CategoryDTO convertToCategoryDTO(final Category category) {
    CategoryDTO categoryDTO = new CategoryDTO(category);
    List<Book> books = bookService.getByBooksCategory(category);
    categoryDTO.setHasBooks(books != null ? !books.isEmpty() : false);
    return categoryDTO;
  }
}
