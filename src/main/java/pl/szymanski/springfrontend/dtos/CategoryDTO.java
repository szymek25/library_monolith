package pl.szymanski.springfrontend.dtos;


import pl.szymanski.springfrontend.model.Category;

public class CategoryDTO {

  private int id;
  private String name;
  private int daysOfRent;
  private boolean hasBooks;
  private Boolean continuationPossible;

  public CategoryDTO(Category category) {
    this.id = category.getId();
    this.name = category.getName();
    this.daysOfRent = category.getDaysOfRent();
    this.continuationPossible = category.isContinuationPossible();
  }

  public CategoryDTO() {

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getDaysOfRent() {
    return daysOfRent;
  }

  public void setDaysOfRent(int daysOfRent) {
    this.daysOfRent = daysOfRent;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean getHasBooks() {
    return hasBooks;
  }

  public void setHasBooks(boolean hasBooks) {
    this.hasBooks = hasBooks;
  }

  public Boolean getContinuationPossible() {
    return continuationPossible;
  }

  public void setContinuationPossible(Boolean continuationPossible) {
    this.continuationPossible = continuationPossible;
  }
}
