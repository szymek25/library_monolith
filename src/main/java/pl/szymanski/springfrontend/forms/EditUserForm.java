package pl.szymanski.springfrontend.forms;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

public class EditUserForm {

  @Email(message = "{users.edit.form.email.invalid}")
  @NotBlank(message = "{users.edit.form.email.blank}")
  private String email;


  @NotBlank(message = "{users.edit.form.name.blank}")
  private String name;

  @NotBlank(message = "{users.edit.form.lastName.blank}")
  private String lastName;

  @NotBlank(message = "{users.edit.form.dayOfBirth.blank}")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private String dayOfBirth;

  private String phone;

  @NotBlank(message = "{users.edit.form.addressLine1.blank}")
  private String addressLine1;

  @NotBlank(message = "{users.edit.form.town.blank}")
  private String town;

  @NotBlank(message = "{users.edit.form.postalCode.blank}")
  private String postalCode;

  @NotBlank(message = "{users.edit.form.roleId.blank}")
  private String roleId;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }
}
