package pl.szymanski.springfrontend.model;

import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import pl.szymanski.springfrontend.enums.OrderStatus;

@Entity
public class BookOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String userEmail;

  private int bookEntryId;

  private String bookInventoryNumber;

  private String bookTitle;

  private Date created;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  private String departmentName;

  private Timestamp timeFromOrderIsReadyForCollect;

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

  public int getBookEntryId() {
    return bookEntryId;
  }

  public void setBookEntryId(int bookEntryId) {
    this.bookEntryId = bookEntryId;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public String getBookInventoryNumber() {
    return bookInventoryNumber;
  }

  public void setBookInventoryNumber(String bookInventoryNumber) {
    this.bookInventoryNumber = bookInventoryNumber;
  }

  public String getBookTitle() {
    return bookTitle;
  }

  public void setBookTitle(String bookTitle) {
    this.bookTitle = bookTitle;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  public Timestamp getTimeFromOrderIsReadyForCollect() {
    return timeFromOrderIsReadyForCollect;
  }

  public void setTimeFromOrderIsReadyForCollect(Timestamp timeFromOrderIsReadyForCollect) {
    this.timeFromOrderIsReadyForCollect = timeFromOrderIsReadyForCollect;
  }
}
