package pl.szymanski.springfrontend.dtos;


import pl.szymanski.springfrontend.model.Role;

public class RoleDTO {

  private long id;
  private String name;
  private String description;

  public RoleDTO(Role role) {
    this.id = role.getId();
    this.name = role.getName();
    this.description = role.getDescription();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
