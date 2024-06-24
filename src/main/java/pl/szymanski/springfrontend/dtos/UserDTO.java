package pl.szymanski.springfrontend.dtos;


import java.sql.Date;
import pl.szymanski.springfrontend.model.Role;
import pl.szymanski.springfrontend.model.User;

public class UserDTO {

  private int id;

  private String email;

  private String password;

  private String name;

  private String lastName;

  private Date dayOfBirth;

  private String phone;

  private String addressLine1;

  private String town;

  private String postalCode;

  private long roleId;

  private String accountType;

  public UserDTO(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.name = user.getName();
    this.lastName = user.getLastName();
    this.dayOfBirth = user.getDayOfBirth();
    this.phone = user.getPhone();
    this.addressLine1 = user.getAddressLine1();
    this.town = user.getTown();
    this.postalCode = user.getPostalCode();
    Role role = user.getRole();
    if (role != null) {
      this.roleId = role.getId();
      this.accountType = role.getDescription();
    }
  }

  public UserDTO() {

  }

  public long getRoleId() {
    return roleId;
  }

  public void setRoleId(long roleId) {
    this.roleId = roleId;
  }

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

  public Date getDayOfBirth() {
    return dayOfBirth;
  }

  public void setDayOfBirth(Date dayOfBirth) {
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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }
}
