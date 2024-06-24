package pl.szymanski.springfrontend.dtos;

import java.sql.Date;
import pl.szymanski.springfrontend.enums.OrderStatus;
import pl.szymanski.springfrontend.model.BookOrder;

public class OrderDTO {

  private int id;

  private String userEmail;

  private String bookTitle;

  private String bookInventoryNumber;

  private Date created;

  private OrderStatus status;

  private boolean currentEmployeeHasPermissionToBook;

  private String departmentName;

  public OrderDTO() {

  }

  public OrderDTO(final BookOrder order) {
    this.id = order.getId();
    this.userEmail = order.getUserEmail();
    this.bookInventoryNumber = order.getBookInventoryNumber();
    this.bookTitle = order.getBookTitle();
    this.created = order.getCreated();
    this.status = order.getStatus();
    this.departmentName = order.getDepartmentName();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getBookTitle() {
    return bookTitle;
  }

  public void setBookTitle(String bookTitle) {
    this.bookTitle = bookTitle;
  }

  public String getBookInventoryNumber() {
    return bookInventoryNumber;
  }

  public void setBookInventoryNumber(String bookInventoryNumber) {
    this.bookInventoryNumber = bookInventoryNumber;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public boolean isCurrentEmployeeHasPermissionToBook() {
    return currentEmployeeHasPermissionToBook;
  }

  public void setCurrentEmployeeHasPermissionToBook(boolean currentEmployeeHasPermissionToBook) {
    this.currentEmployeeHasPermissionToBook = currentEmployeeHasPermissionToBook;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }
}
