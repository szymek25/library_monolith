package pl.szymanski.springfrontend.dtos;

public class CreateOrderDTO {

  private int bookEntryId;
  private String userEmail;

  public CreateOrderDTO() {

  }

  public CreateOrderDTO(final int bookEntryId, final String userEmail) {
    this.bookEntryId = bookEntryId;
    this.userEmail = userEmail;
  }

  public int getBookEntryId() {
    return bookEntryId;
  }

  public void setBookEntryId(int bookEntryId) {
    this.bookEntryId = bookEntryId;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }
}
