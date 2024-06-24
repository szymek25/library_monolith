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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.constants.ExceptionCodes;
import pl.szymanski.springfrontend.dtos.DepartmentDTO;
import pl.szymanski.springfrontend.dtos.UserDTO;
import pl.szymanski.springfrontend.exceptions.DepartmentException;
import pl.szymanski.springfrontend.facade.DepartmentFacade;
import pl.szymanski.springfrontend.facade.UserFacade;
import pl.szymanski.springfrontend.forms.DepartmentForm;

@Controller
@RequestMapping("/departments")
public class DepartmentController extends AbstractPageController {

  @Autowired
  private DepartmentFacade departmentFacade;

  @Autowired
  private UserFacade userFacade;

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String allDepartments(@RequestParam("page") final Optional<Integer> page,
      final Model model) {
    final Integer currentPage = page.orElse(0);
    final PageRequest pageable = new PageRequest(currentPage,
        ApplicationConstants.DEFAULT_PAGE_SIZE);
    final Page<DepartmentDTO> paginatedDepartments = departmentFacade
        .getPaginatedDepartments(pageable);

    addPaginationResult(currentPage, "departments", paginatedDepartments, model);
    return "departments";
  }

  @RequestMapping(value = "new", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_MANAGER')")
  public String addNewDepartmentView(final Model model) {
    model.addAttribute("addDepartmentForm", new DepartmentForm());

    return "addNewDepartment";
  }

  @RequestMapping(value = "/new", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_MANAGER')")
  public String addNewDepartment(
      @ModelAttribute("addDepartmentForm") final @Valid DepartmentForm form,
      final BindingResult result) {

    if (result.hasErrors()) {
      return "addNewDepartment";
    }

    try {
      departmentFacade.addNewDepartment(form);
    } catch (DepartmentException e) {
      result.rejectValue("name", "departments.add.departmentExists");
      return "addNewDepartment";
    }

    return REDIRECT_PREFIX + "/departments/list";
  }

  @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_MANAGER')")
  public String editDepartmentView(@PathVariable("id") int id, Model model) {
    DepartmentDTO departmentDTO = departmentFacade.getDepartmentById(id);
    DepartmentForm departmentForm = prepareDepartmentForm(departmentDTO);
    model.addAttribute("department", departmentDTO);
    model.addAttribute("editDepartmentForm", departmentForm);

    return "departmentEdit";
  }

  @RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_MANAGER')")
  public String editDepartment(@PathVariable("id") final int id,
      @ModelAttribute("editDepartmentForm") @Valid final DepartmentForm form,
      final BindingResult result,
      final RedirectAttributes redirect) {
    if (result.hasErrors()) {
      return "departmentEdit";
    }

    try {
      departmentFacade.updateDepartment(id, form);

    } catch (DepartmentException e) {
      if (ExceptionCodes.DEPARTMENT_ARLEADY_EXSIST.equals(e.getMessage())) {
        result.rejectValue("name", "departments.edit.departmentExists");
        return "departmentEdit";

      } else {
        addFlashErrorMessage("department.edit.error", redirect);
        return REDIRECT_PREFIX + "/departments/list";
      }

    }

    return REDIRECT_PREFIX + "/departments/list";
  }

  @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_MANAGER')")
  public String deleteBook(@PathVariable("id") final int id) {
    departmentFacade.deleteDepartment(id);

    return REDIRECT_PREFIX + "/departments/list";
  }

  @RequestMapping(value = "/selectEmployee/{departmentId}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_MANAGER')")
  public String selectEmployeePage(@PathVariable("departmentId") final int departmentId,
      @RequestParam("page") final Optional<Integer> page, final Model model) {
    final DepartmentDTO departmentDTO = departmentFacade.getDepartmentById(departmentId);
    if (departmentDTO == null) {
      return REDIRECT_PREFIX + "/departments/list";
    }
    model.addAttribute("department", departmentDTO);
    final Integer currentPage = page.orElse(0);
    final Pageable pageable = new PageRequest(currentPage, ApplicationConstants.DEFAULT_PAGE_SIZE);
    final Page<UserDTO> paginatedUser = userFacade.getPaginatedLibraryEmployees(pageable);
    addPaginationResult(currentPage, "users", paginatedUser, model);

    return "selectEmployee";
  }

  @RequestMapping(value = "/selectEmployee", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_MANAGER')")
  public String selectEmployee(@RequestParam("userId") final int userId,
      @RequestParam("departmentId") final int departmentId, final RedirectAttributes redirect) {

    try {
      departmentFacade.addEmployeeToDepartment(userId, departmentId);
    } catch (DepartmentException e) {
      addFlashErrorMessage("departments.employees.add.error", redirect);
      return REDIRECT_PREFIX + "departments/list";
    }

    addFlashConfMessage("departments.employees.add.success", redirect);
    return REDIRECT_PREFIX + "/departments/edit/" + departmentId;
  }

  @RequestMapping(value = "/removeEmployee", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_MANAGER')")
  public String removeEmployee(@RequestParam("userId") final int userId,
      @RequestParam("departmentId") final int departmentId,
      final RedirectAttributes redirect) {
    try {
      departmentFacade.removeEmployeeFromDepartment(userId);
    } catch (DepartmentException e) {
      addFlashErrorMessage("department.edit.employees.remove.error", redirect);
    }

    addFlashConfMessage("department.edit.employees.remove.success", redirect);
    return REDIRECT_PREFIX + "/departments/edit/" + departmentId;
  }

  protected DepartmentForm prepareDepartmentForm(final DepartmentDTO departmentDTO) {
    final DepartmentForm form = new DepartmentForm();
    form.setName(departmentDTO.getName());
    form.setAddressLine1(departmentDTO.getAddressLine1());
    form.setPostalCode(departmentDTO.getPostalCode());
    form.setTown(departmentDTO.getTown());
    form.setIpAddress(departmentDTO.getIpAddress());

    return form;
  }
}
