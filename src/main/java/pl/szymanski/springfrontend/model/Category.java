package pl.szymanski.springfrontend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column
  private String name;

  @Column
  private int daysOfRent;

  @Column(columnDefinition = "boolean default false")
  private Boolean continuationPossible;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public Boolean isContinuationPossible() {
    return continuationPossible;
  }

  public void setContinuationPossible(Boolean continuationPossible) {
    this.continuationPossible = continuationPossible;
  }
}
