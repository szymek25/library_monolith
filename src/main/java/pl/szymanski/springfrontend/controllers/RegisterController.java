package pl.szymanski.springfrontend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.szymanski.springfrontend.exceptions.DuplicatedUserException;
import pl.szymanski.springfrontend.facade.UserFacade;
import pl.szymanski.springfrontend.forms.RegisterForm;

import javax.validation.Valid;

@Controller
public class RegisterController extends AbstractPageController {

  @Autowired
  private UserFacade userFacade;

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String registerPage(final Model model) {
    model.addAttribute("registerForm", new RegisterForm());

    return "register";
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public String register(@ModelAttribute("registerForm") @Valid final RegisterForm form,
      final BindingResult result, final Model model) {
    if (result.hasErrors()) {
      return "register";
    }


    try {
      if (userFacade.registerUser(form)) {
        addGlobalConfMessage("register.success", model);
      } else {
        addGlobalConfMessage("register.failed", model);
      }
    } catch (DuplicatedUserException e) {
      addGlobalErrorMessage("register.userExists", model);

      return "register";
    }
    return "home";
  }
}
