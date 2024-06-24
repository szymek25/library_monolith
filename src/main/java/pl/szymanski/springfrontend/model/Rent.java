package pl.szymanski.springfrontend.model;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Rent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String userEmail;

  private int bookEntryId;

  private String bookInventoryNumber;

  private String bookTitle;

  private String bookIsbn;

  private Date startDate;

  private Date endDate;

  private Date returnDate;

  private String bookPhysicalState;

  private String departmentName;

  @Column(columnDefinition = "boolean default false")
  private Boolean prolongationPerformed;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(Date returnDate) {
    this.returnDate = returnDate;
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

  public String getBookIsbn() {
    return bookIsbn;
  }

  public void setBookIsbn(String bookIsbn) {
    this.bookIsbn = bookIsbn;
  }

  public int getBookEntryId() {
    return bookEntryId;
  }

  public void setBookEntryId(int bookEntryId) {
    this.bookEntryId = bookEntryId;
  }

  public String getBookInventoryNumber() {
    return bookInventoryNumber;
  }

  public void setBookInventoryNumber(String bookInventoryNumber) {
    this.bookInventoryNumber = bookInventoryNumber;
  }

  public String getBookPhysicalState() {
    return bookPhysicalState;
  }

  public void setBookPhysicalState(String bookPhysicalState) {
    this.bookPhysicalState = bookPhysicalState;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  public Boolean getProlongationPerformed() {
    return prolongationPerformed;
  }

  public void setProlongationPerformed(Boolean prolongationPerformed) {
    this.prolongationPerformed = prolongationPerformed;
  }
}
