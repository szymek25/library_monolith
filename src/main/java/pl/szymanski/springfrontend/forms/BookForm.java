package pl.szymanski.springfrontend.forms;

import javax.validation.constraints.NotBlank;

public class BookForm {

  @NotBlank(message = "{books.add.form.isbn.blank}")
  private String isbn;

  @NotBlank(message = "{books.add.form.title.blank}")
  private String title;

  @NotBlank(message = "{books.add.form.author.blank}")
  private String author;

  @NotBlank(message = "{books.add.form.publicationYear.blank}")
  private String publicationYear;

  @NotBlank(message = "{books.add.form.category.blank}")
  private String categoryId;

  @NotBlank(message = "{books.add.form.publisher.blank}")
  private String publisher;

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
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

  public String getPublicationYear() {
    return publicationYear;
  }

  public void setPublicationYear(String publicationYear) {
    this.publicationYear = publicationYear;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }
}
