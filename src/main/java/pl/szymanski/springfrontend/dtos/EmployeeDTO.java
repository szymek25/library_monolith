package pl.szymanski.springfrontend.dtos;

import pl.szymanski.springfrontend.model.User;

public class EmployeeDTO {

  private int id;
  private String email;
  private String name;
  private String lastName;

  public EmployeeDTO(User user){
    this.id = user.getId();
    this.email = user.getEmail();
    this.name = user.getName();
    this.lastName = user.getLastName();
  }

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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
