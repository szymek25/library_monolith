package pl.szymanski.springfrontend.api.dto;

public class BookBibsDTO {

  private String isbnIssn;

  private String title;

  private String author;

  private String publisher;

  private String publicationYear;

  public String getIsbnIssn() {
    return isbnIssn;
  }

  public void setIsbnIssn(String isbnIssn) {
    this.isbnIssn = isbnIssn;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getPublicationYear() {
    return publicationYear;
  }

  public void setPublicationYear(String publicationYear) {
    this.publicationYear = publicationYear;
  }
}
