package pl.szymanski.springfrontend.forms;

import javax.validation.constraints.NotBlank;

public class DepartmentForm {

  @NotBlank(message = "{departments.add.form.name.blank}")
  private String name;

  @NotBlank(message = "{departments.add.form.addressLine1.blank}")
  private String addressLine1;

  @NotBlank(message = "{departments.add.form.town.blank}")
  private String town;

  @NotBlank(message = "{departments.add.form.postalCode.blank}")
  private String postalCode;

  private String ipAddress;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }
}
