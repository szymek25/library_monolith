package pl.szymanski.springfrontend.dtos;


import java.sql.Date;
import pl.szymanski.springfrontend.model.Rent;

public class RentDTO {

  private int id;

  private String userEmail;

  private String bookTitle;

  private String bookIsbn;

  private String bookInventoryNumber;

  private String bookPhysicalState;

  private Date startDate;

  private Date endDate;

  private Date returnDate;

  private boolean currentEmployeeHasPermissionToBook;

  private String departmentName;

  private Boolean prolongationPossible;

  public RentDTO(final Rent rent) {
    this.id = rent.getId();
    this.userEmail = rent.getUserEmail();
    this.bookIsbn = rent.getBookIsbn();
    this.bookTitle = rent.getBookTitle();
    this.startDate = rent.getStartDate();
    this.endDate = rent.getEndDate();
    this.returnDate = rent.getReturnDate();
    this.bookInventoryNumber = rent.getBookInventoryNumber();
    this.bookPhysicalState = rent.getBookPhysicalState();
    this.departmentName = rent.getDepartmentName();
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

  public String getBookIsbn() {
    return bookIsbn;
  }

  public void setBookIsbn(String bookIsbn) {
    this.bookIsbn = bookIsbn;
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

  public Boolean getProlongationPossible() {
    return prolongationPossible;
  }

  public void setProlongationPossible(Boolean prolongationPossible) {
    this.prolongationPossible = prolongationPossible;
  }
}
