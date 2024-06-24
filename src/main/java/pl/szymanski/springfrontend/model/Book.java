package pl.szymanski.springfrontend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column
  private String isbn;

  @Column
  private String title;

  @Column
  private String author;

  @Column
  private String publicationYear;

  @OneToOne(fetch = FetchType.EAGER)
  private Category category;

  @Column
  private String publisher;

  @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
  private List<BookEntry> entries;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  @JsonProperty("categoryId")
  public int getRoleId() {
    return this.category.getId();
  }

  public List<BookEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<BookEntry> entries) {
    this.entries = entries;
  }
}
