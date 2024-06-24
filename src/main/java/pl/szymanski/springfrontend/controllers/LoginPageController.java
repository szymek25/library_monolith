package pl.szymanski.springfrontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginPageController {


  @RequestMapping("/login")
  public String login(
      @RequestParam(name = "error", defaultValue = "false", required = false) String error,
      @RequestParam(name = "ipRestrict", defaultValue = "false", required = false) String ipRestrict,
      @RequestParam(name = "emplNonAssigned", defaultValue = "false", required = false) String employeeNonAssigned,
      Model model) {
    model.addAttribute("error", error);
    model.addAttribute("ipRestrict", ipRestrict);
    model.addAttribute("employeeNonAssigned", employeeNonAssigned);
    return "login";
  }
}
