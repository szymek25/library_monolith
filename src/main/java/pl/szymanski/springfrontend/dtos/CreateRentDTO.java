package pl.szymanski.springfrontend.dtos;

public class CreateRentDTO {

  private int bookEntryId;
  private int userId;

  public CreateRentDTO() {

  }

  public CreateRentDTO(final int bookEntryId, final int userId) {
    this.bookEntryId = bookEntryId;
    this.userId = userId;
  }

  public int getBookEntryId() {
    return bookEntryId;
  }

  public void setBookEntryId(int bookEntryId) {
    this.bookEntryId = bookEntryId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }
}
