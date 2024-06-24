package pl.szymanski.springfrontend.controllers;

import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.constants.ExceptionCodes;
import pl.szymanski.springfrontend.dtos.CategoryDTO;
import pl.szymanski.springfrontend.exceptions.CategoryException;
import pl.szymanski.springfrontend.facade.CategoryFacade;
import pl.szymanski.springfrontend.forms.CategoryForm;

@Controller
@RequestMapping("/categories")
public class CategoryController extends AbstractPageController {

  @Autowired
  private CategoryFacade categoryFacade;

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String allCategories(@RequestParam("page") final Optional<Integer> page,
      final Model model) {
    final Integer currentPage = page.orElse(0);
    final Pageable pageable = new PageRequest(currentPage, ApplicationConstants.DEFAULT_PAGE_SIZE);
    final Page<CategoryDTO> paginatedCategories = categoryFacade.getCategoriesForListing(pageable);

    addPaginationResult(currentPage, "categories", paginatedCategories, model);
    return "categories";
  }

  @RequestMapping(value = "new", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String addNewCategoryView(final Model model) {
    model.addAttribute("addCategoryForm", new CategoryForm());

    return "newCategory";
  }

  @RequestMapping(value = "new", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String addCategory(@ModelAttribute("addCategoryForm") @Valid final CategoryForm form, final
  BindingResult result, final Model model) {
    if (result.hasErrors()) {
      return "newCategory";
    }

    try {
      categoryFacade.addNewCategory(form);

    } catch (CategoryException e) {
      addGlobalErrorMessage("categories.add.categoryExists", model);
      return "newCategory";
    }

    return REDIRECT_PREFIX + "/categories/list";
  }

  @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String editCategoryPage(@PathVariable("id") final int id,
      final Model model) {
    CategoryDTO category = categoryFacade.getCategoryById(id);
    if (category == null) {
      return REDIRECT_PREFIX + "/categories/list";
    }

    CategoryForm categoryForm = new CategoryForm();
    categoryForm.setName(category.getName());
    categoryForm.setDaysOfRent(category.getDaysOfRent());
    categoryForm.setContinuationPossible(category.getContinuationPossible());
    model.addAttribute("editCategoryForm", categoryForm);

    return "categoryEdit";
  }

  @RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String editCategory(@PathVariable("id") final int id,
      @ModelAttribute("editCategoryForm") @Valid final CategoryForm form, final BindingResult result,
      final Model model) {
    if (result.hasErrors()) {
      return "categoryEdit";
    }

    try {
      categoryFacade.updateCategory(id, form);

    } catch (CategoryException e) {
      switch (e.getMessage()) {
        case ExceptionCodes.CATEGORY_ARLEADY_EXSIST:
          addGlobalErrorMessage("categories.edit.categoryExists", model);
          return "categoryEdit";

        case ExceptionCodes.CATEGORY_DOES_NOT_EXSIST:
          addGlobalErrorMessage("categories.edit.categoryDoesNotExists", model);
          return "categoryEdit";

      }
    }

    return REDIRECT_PREFIX + "/categories/list";
  }


  @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String deleteCategory(@PathVariable("id") final int id) {
    categoryFacade.deleteCategory(id);

    return REDIRECT_PREFIX + "/categories/list";
  }

}
