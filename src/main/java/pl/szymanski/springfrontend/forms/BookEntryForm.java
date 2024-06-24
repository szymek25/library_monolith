package pl.szymanski.springfrontend.forms;

import javax.validation.constraints.NotBlank;

public class BookEntryForm {

  private String bookId;

  @NotBlank(message = "{books.detail.entryform.inventoryNumber.blank}")
  private String inventoryNumber;

  @NotBlank(message = "{books.detail.entryform.signature.blank}")
  private String signature;

  @NotBlank(message = "{books.detail.entryform.physicalState.blank}")
  private String physicalState;

  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
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
}
