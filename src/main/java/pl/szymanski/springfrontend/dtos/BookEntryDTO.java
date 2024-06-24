package pl.szymanski.springfrontend.dtos;

import pl.szymanski.springfrontend.model.Book;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.model.Department;

public class BookEntryDTO {

  private int id;

  private int bookId;

  private String inventoryNumber;

  private String signature;

  private String physicalState;

  private BookDTO book;

  private boolean isBookRented;

  private boolean currentEmployeeHasPermissionToBook;

  private String departmentName;

  private int departmentId;

  private boolean userReservedBook;

  private boolean userOrderedBook;

  public BookEntryDTO(BookEntry bookEntry) {
    this.id = bookEntry.getBookEntry_id();
    this.inventoryNumber = bookEntry.getInventoryNumber();
    this.signature = bookEntry.getSignature();
    this.physicalState = bookEntry.getPhysicalState();
    final Book book = bookEntry.getBook();
    if (book != null) {
      final BookDTO bookDTO = new BookDTO();
      bookDTO.setTitle(book.getTitle());
      bookDTO.setIsbn(book.getIsbn());
      bookDTO.setPublicationYear(book.getPublicationYear());
      bookDTO.setAuthor(book.getAuthor());
      this.book = bookDTO;
    }
    final Department department = bookEntry.getDepartment();
    if (department != null) {
      this.departmentName = department.getName();
      this.departmentId = department.getDepartmentId();
    }
  }

  public BookEntryDTO() {

  }

  public int getBookId() {
    return bookId;
  }

  public void setBookId(int bookId) {
    this.bookId = bookId;
  }

  public String getInventoryNumber() {
    return inventoryNumber;
  }

  public void setInventoryNumber(String inventoryNumber) {
    this.inventoryNumber = inventoryNumber;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getPhysicalState() {
    return physicalState;
  }

  public void setPhysicalState(String physicalState) {
    this.physicalState = physicalState;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public BookDTO getBook() {
    return book;
  }

  public void setBook(BookDTO book) {
    this.book = book;
  }

  public boolean getIsBookRented() {
    return isBookRented;
  }

  public void setBookRented(boolean bookRented) {
    isBookRented = bookRented;
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

  public int getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(int departmentId) {
    this.departmentId = departmentId;
  }

  public boolean isUserReservedBook() {
    return userReservedBook;
  }

  public void setUserReservedBook(boolean userReservedBook) {
    this.userReservedBook = userReservedBook;
  }

  public boolean isUserOrderedBook() {
    return userOrderedBook;
  }

  public void setUserOrderedBook(boolean userOrderedBook) {
    this.userOrderedBook = userOrderedBook;
  }
}
