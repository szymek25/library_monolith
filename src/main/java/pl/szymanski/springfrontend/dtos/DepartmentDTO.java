package pl.szymanski.springfrontend.dtos;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import pl.szymanski.springfrontend.model.Department;

public class DepartmentDTO {

  private int id;

  private String name;

  private String addressLine1;

  private String town;

  private String postalCode;

  private List<EmployeeDTO> employees;

  private String ipAddress;

  public DepartmentDTO() {
    //Default Empty Constructor
  }

  public DepartmentDTO(final Department department) {
    this.id = department.getDepartmentId();
    this.name = department.getName();
    this.addressLine1 = department.getAddressLine1();
    this.town = department.getTown();
    this.postalCode = department.getPostalCode();
    this.ipAddress = department.getIpAddress();
    if (CollectionUtils.isNotEmpty(department.getEmployees())) {
      this.employees = department.getEmployees().stream()
          .map(EmployeeDTO::new)
          .collect(Collectors.toList());
    }
  }

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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<EmployeeDTO> getEmployees() {
    return employees;
  }

  public void setEmployees(List<EmployeeDTO> employees) {
    this.employees = employees;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }
}
