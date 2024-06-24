package pl.szymanski.springfrontend.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import pl.szymanski.springfrontend.validation.adnotations.FieldMatch;

@FieldMatch.List({
    @FieldMatch(first = "password", second = "confirmPassword", message = "{users.edit.updatepass.form.password.missmatch}")})
public class UpdatePasswordForm {


  @NotBlank(message = "{users.edit.updatepass.form.password.blank}")
  @Size(min = 6, max = 25, message = "{users.edit.updatepass.form.password.size}")
  private String password;

  @NotBlank(message = "{users.edit.updatepass.form.password.blank}")
  @Size(min = 6, max = 25, message = "{users.edit.updatepass.form.password.size}")
  private String confirmPassword;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }
}
