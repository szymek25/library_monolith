package pl.szymanski.springfrontend.dtos;


import pl.szymanski.springfrontend.model.Role;

public class RoleDTO {

  private String id;
  private String name;
  private String description;

  public RoleDTO(Role role) {
    if(role != null) {
      this.id = String.valueOf(role.getId());
      this.name = role.getName();
      this.description = role.getDescription();
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
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
