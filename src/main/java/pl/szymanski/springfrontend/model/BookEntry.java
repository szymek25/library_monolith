package pl.szymanski.springfrontend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class BookEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int bookEntry_id;

  @ManyToOne
  @JoinColumn(name="id", nullable=false)
  private Book book;

  @Column
  private String inventoryNumber;

  @Column
  private String signature;

  @Column
  private String physicalState;

  @ManyToOne
  private Department department;

  public int getBookEntry_id() {
    return bookEntry_id;
  }

  public void setBookEntry_id(int bookEntry_id) {
    this.bookEntry_id = bookEntry_id;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
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

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }
}
