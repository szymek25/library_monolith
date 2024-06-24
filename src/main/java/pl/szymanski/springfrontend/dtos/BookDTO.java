package pl.szymanski.springfrontend.dtos;


import java.util.List;
import java.util.stream.Collectors;
import pl.szymanski.springfrontend.model.Book;
import pl.szymanski.springfrontend.model.Category;

public class BookDTO {

  private int id;

  private String isbn;

  private String title;

  private String author;

  private String publicationYear;

  private int categoryId;

  private CategoryDTO category;

  private String publisher;

  private List<BookEntryDTO> entries;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public BookDTO(Book book) {
    this.id = book.getId();
    this.isbn = book.getIsbn();
    this.title = book.getTitle();
    this.author = book.getAuthor();
    this.publicationYear = book.getPublicationYear();
    this.publisher = book.getPublisher();
    Category category = book.getCategory();
    if (category != null) {
      this.category = new CategoryDTO(category);
      this.categoryId = category.getId();
    }
  }

  public BookDTO() {

  }

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

  public int getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public CategoryDTO getCategory() {
    return category;
  }

  public void setCategory(CategoryDTO category) {
    this.category = category;
  }

  public List<BookEntryDTO> getEntries() {
    return entries;
  }

  public void setEntries(List<BookEntryDTO> entries) {
    this.entries = entries;
  }
}
