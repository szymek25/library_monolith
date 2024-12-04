package pl.szymanski.springfrontend.controllers;

import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.szymanski.springfrontend.barcode.BarcodeDecoder;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.dtos.UserDTO;
import pl.szymanski.springfrontend.exceptions.BarcodeDecodingException;
import pl.szymanski.springfrontend.exceptions.DuplicatedUserException;
import pl.szymanski.springfrontend.facade.RoleFacade;
import pl.szymanski.springfrontend.facade.UserFacade;
import pl.szymanski.springfrontend.forms.AddUserForm;
import pl.szymanski.springfrontend.forms.EditUserForm;
import pl.szymanski.springfrontend.forms.RegisterForm;
import pl.szymanski.springfrontend.forms.UpdatePasswordForm;
import pl.szymanski.springfrontend.pdf.GeneratePDFUtil;

@Controller
@RequestMapping("/users")
public class UserPageController extends AbstractPageController {

  private static final Logger LOG = LoggerFactory.getLogger(UserPageController.class);

  @Autowired
  private UserFacade userFacade;

  @Autowired
  private RoleFacade roleFacade;

  @Autowired
  private GeneratePDFUtil generatePDFUtil;


  @RequestMapping(value = "/list", method = RequestMethod.GET)
  @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_EMPLOYEE')")
  public String allUsers(@RequestParam("page") final Optional<Integer> page,
      final Model model) {
    final Integer currentPage = page.orElse(0);
    final Pageable pageable = new PageRequest(currentPage, ApplicationConstants.DEFAULT_PAGE_SIZE);
    Page<UserDTO> paginatedUser;
    if (userFacade.isCurrentUserManager()) {
      paginatedUser = userFacade.getPaginatedUser(pageable);
    } else {
      paginatedUser = userFacade.getPaginatedLibraryCustomers(pageable);
    }

    addPaginationResult(currentPage, "users", paginatedUser, model);
    return "users";
  }

  @PreAuthorize("hasRole('ROLE_MANAGER')")
  @RequestMapping(value = "list", method = RequestMethod.POST)
  public String findUserByBarCode(@RequestParam("barcode") final String barcode,
      final Model model, final RedirectAttributes redirect) {
    Integer decodedBarcode;
    try {
      decodedBarcode = BarcodeDecoder.decodeUserBarCode(barcode);
    } catch (BarcodeDecodingException e) {
      LOG.warn(e.getMessage());
      addFlashErrorMessage("search.with.barcode.invalidBarcode", redirect);
      return REDIRECT_PREFIX + "/users/list";
    }
    final UserDTO user = userFacade.getUserById(String.valueOf(decodedBarcode));
    if (user == null) {
      addFlashErrorMessage("users.list.findUser.notFound", redirect);
      return REDIRECT_PREFIX + "/users/list";
    }

    model.addAttribute("users", Arrays.asList(user));

    return "users";
  }

  @RequestMapping(value = "new", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_MANAGER')")
  public String addNewUserView(final Model model) {
    model.addAttribute("addUserForm", new AddUserForm());
    model.addAttribute("roles", roleFacade.getAllRoles());

    return "addNewUser";
  }

  @RequestMapping(value = "/new", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_MANAGER')")
  public String addNewUser(@ModelAttribute("addUserForm") final @Valid AddUserForm form,
      BindingResult result, final Model model) {

    if (result.hasErrors()) {
      addSelectedRoleToModel(form.getRoleId(), model);

      return "addNewUser";
    }

    try {
      if (!userFacade.addNewUser(form)) {
        addSelectedRoleToModel(form.getRoleId(), model);
        addGlobalErrorMessage("users.add.error", model);

        return "addNewUser";
      }
    } catch (DuplicatedUserException e) {
      addSelectedRoleToModel(form.getRoleId(), model);
      addGlobalErrorMessage("users.add.userExists", model);

      return "addNewUser";
    }

    return REDIRECT_PREFIX + "/users/list";
  }

  @RequestMapping(value = "newCustomer", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String addNewCustomerView(final Model model) {
    model.addAttribute("addUserForm", new RegisterForm());

    return "addNewCustomer";
  }

  @RequestMapping(value = "/newCustomer", method = RequestMethod.POST)
  public String register(@ModelAttribute("addUserForm") @Valid final RegisterForm form,
      final BindingResult result) {
    if (result.hasErrors()) {
      return "addNewCustomer";
    }

    String email = form.getEmail();
    if (!StringUtils.isEmpty(email) && userFacade.existsUserByEmail(email)) {
      result.rejectValue("email", "users.add.userExists");

      return "addNewCustomer";
    }

    userFacade.registerUser(form);

    return REDIRECT_PREFIX + "/users/list";
  }

  @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
  @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_EMPLOYEE')")
  public String editUserView(@PathVariable("id") Optional<String> id, Model model) {
    if (id.isPresent()) {
      UserDTO user = userFacade.getUserById(id.get());
      if (user == null) {
        return "users";
      }
      EditUserForm userForm = prepareEditUserForm(user);
      model.addAttribute("editUserForm", userForm);
      addUpdatePasswordForm(id, model);
      addSelectedRoleToModel(userForm.getRoleId(), model);

    } else {
      return "users";
    }

    return "userEdit";
  }

  private EditUserForm prepareEditUserForm(final UserDTO user) {
    EditUserForm userForm = new EditUserForm();
    userForm.setEmail(user.getEmail());
    userForm.setName(user.getName());
    userForm.setLastName(user.getLastName());
    userForm.setAddressLine1(user.getAddressLine1());
    userForm.setTown(user.getTown());
    userForm.setPostalCode(user.getPostalCode());
    final Date date = user.getDayOfBirth();
    if (date != null) {
      userForm.setDayOfBirth(date.toString());
    }
    userForm.setPhone(user.getPhone());
    userForm.setRoleId(String.valueOf(user.getRoleId()));

    return userForm;
  }

  @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
  @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_EMPLOYEE')")
  public String editUser(@PathVariable("id") Optional<String> id,
      @ModelAttribute("editUserForm") final @Valid EditUserForm form,
      final BindingResult result, final Model model) {

    if (!id.isPresent()) {
      return REDIRECT_PREFIX + "/users/list";
    }

    final String userId = id.get();
    final UserDTO user = userFacade.getUserById(userId);

    if (result.hasErrors()) {
      addSelectedRoleToModel(form.getRoleId(), model);

      return "userEdit";
    }

    final String email = form.getEmail();
    if (!StringUtils.isEmpty(email) && !email.equals(user.getEmail()) && userFacade
        .existsUserByEmail(email)) {
      form.setEmail(user.getEmail());
      addSelectedRoleToModel(form.getRoleId(), model);
      addGlobalErrorMessage("users.edit.userExists", model);
      addUpdatePasswordForm(id, model);

      return "userEdit";
    }

    if (!userFacade.updateUser(userId, form)) {
      addSelectedRoleToModel(form.getRoleId(), model);
      addGlobalErrorMessage("users.edit.error", model);
      addUpdatePasswordForm(id, model);

      return "userEdit";
    }

    return REDIRECT_PREFIX + "/users/list";
  }

  private void addUpdatePasswordForm(Optional<String> id, Model model) {
    model.addAttribute("updatePasswordForm", new UpdatePasswordForm());
    model.addAttribute("userId", id.get());
  }

  @RequestMapping(value = "/edit/{id}/updatePassword", method = RequestMethod.POST)
  @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE','ROLE_MANAGER')")
  public String updatePassword(@PathVariable("id") Optional<String> id,
      @ModelAttribute("updatePasswordForm") final @Valid UpdatePasswordForm form,
      final BindingResult result, final Model model) {

    if (!id.isPresent()) {
      return REDIRECT_PREFIX + "/users/list";
    }

    final String userId = id.get();

    if (result.hasErrors()) {
      EditUserForm userForm = prepareEditUserForm(userFacade.getUserById(String.valueOf(id.get())));
      model.addAttribute("editUserForm", userForm);
      model.addAttribute("userId", id.get());
      addSelectedRoleToModel(userForm.getRoleId(), model);
      return "userEdit";
    }
    if (userFacade.updateUserPassword(userId, form.getPassword())) {
      addGlobalErrorMessage("user.edit.password.error", model);
    }

    return REDIRECT_PREFIX + "/users/list";
  }

  @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_MANAGER')")
  public String deleteUser(@PathVariable("id") final Optional<String> id) {
    if (!id.isPresent()) {
      return REDIRECT_PREFIX + "/users/list";
    }

    userFacade.delete(id.get());

    return REDIRECT_PREFIX + "/users/list";
  }

  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  @RequestMapping(value = "/printUserLabel/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<InputStreamResource> printUserLabel(final Locale locale,
      @PathVariable("id") final int id) {
    final UserDTO userDTO = userFacade.getUserById(String.valueOf(id));
    ByteArrayInputStream bis = generatePDFUtil.generateUserLabel(userDTO, locale);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Disposition", "inline; filename=citiesreport.pdf");

    return ResponseEntity
        .ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_PDF)
        .body(new InputStreamResource(bis));
  }

  private void addSelectedRoleToModel(final String roleId, final Model model) {
    model.addAttribute("roles", roleFacade.getAllRoles());
    model.addAttribute("selectedRole", roleId);
  }

}
