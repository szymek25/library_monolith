package pl.szymanski.springfrontend.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import pl.szymanski.springfrontend.validation.adnotations.FieldMatch;

@FieldMatch.List({
    @FieldMatch(first = "password", second = "confirmPassword", message = "{register.form.password.missmatch}")})
public class RegisterForm {

  @Email(message = "{register.form.email.invalid}")
  @NotBlank(message = "{register.form.email.blank}")
  private String email;

  @NotBlank(message = "{register.form.password.blank}")
  @Size(min = 6, max = 25, message = "{register.form.password.size}")
  private String password;

  @NotBlank(message = "{register.form.password.blank}")
  @Size(min = 6, max = 25, message = "{register.form.password.size}")
  private String confirmPassword;

  @NotBlank(message = "{register.form.name.blank}")
  private String name;

  @NotBlank(message = "{register.form.lastName.blank}")
  private String lastName;

  @NotBlank(message = "{register.form.dayOfBirth.blank}")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private String dayOfBirth;

  private String phone;

  @NotBlank(message = "{register.form.addressLine1.blank}")
  private String addressLine1;

  @NotBlank(message = "{register.form.town.blank}")
  private String town;

  @NotBlank(message = "{register.form.postalCode.blank}")
  private String postalCode;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDayOfBirth() {
    return dayOfBirth;
  }

  public void setDayOfBirth(String dayOfBirth) {
    this.dayOfBirth = dayOfBirth;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public String getTown() {
    return town;
  }

  public void setTown(String town) {
    this.town = town;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }
}
