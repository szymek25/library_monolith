package pl.szymanski.springfrontend.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.szymanski.springfrontend.constants.ExceptionCodes;
import pl.szymanski.springfrontend.constants.ResponseCodes;
import pl.szymanski.springfrontend.dao.CategoryDao;
import pl.szymanski.springfrontend.dtos.CategoryDTO;
import pl.szymanski.springfrontend.exceptions.CategoryException;
import pl.szymanski.springfrontend.model.Category;
import pl.szymanski.springfrontend.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

  @Autowired
  private CategoryDao categoryDao;

  @Override
  public boolean addNewCategory(CategoryDTO categoryDTO) throws CategoryException {
    if (categoryDao.existsByName(categoryDTO.getName())) {
      throw new CategoryException(ExceptionCodes.CATEGORY_ARLEADY_EXSIST);
    }

    Category newCategory = new Category();
    newCategory.setName(categoryDTO.getName());
    newCategory.setDaysOfRent(categoryDTO.getDaysOfRent());
    newCategory.setContinuationPossible(categoryDTO.getContinuationPossible());
    categoryDao.save(newCategory);

    return true;
  }

  @Override
  public List<Category> getAll() {
    List<Category> list = new ArrayList<>();
    categoryDao.findAll().iterator().forEachRemaining(list::add);
    return list;
  }

  @Override
  public Page<Category> getAllPaginated(Pageable pageable) {
    return categoryDao.findAll(pageable);
  }

  @Override
  public String deleteCategory(int id) {
    categoryDao.deleteById(id);

    return ResponseCodes.SUCCESS;
  }

  @Override
  public Category getCategoryById(int id) {
    return categoryDao.findById(id).get();
  }

  @Override
  public boolean updateCategory(int id, CategoryDTO categoryDTO) throws CategoryException {
    Category category = getCategoryById(id);
    if (category == null) {
      throw new CategoryException(ExceptionCodes.CATEGORY_DOES_NOT_EXSIST);
    }

    String oldName = category.getName();
    String newName = categoryDTO.getName();
    if (!StringUtils.isEmpty(newName)) {
      if (!newName.equals(oldName) && categoryDao.existsByName(newName)) {
        throw new CategoryException(ExceptionCodes.CATEGORY_ARLEADY_EXSIST);
      }
    }

    category.setName(categoryDTO.getName());
    category.setDaysOfRent(categoryDTO.getDaysOfRent());
    category.setContinuationPossible(categoryDTO.getContinuationPossible());
    categoryDao.save(category);

    return true;
  }
}
