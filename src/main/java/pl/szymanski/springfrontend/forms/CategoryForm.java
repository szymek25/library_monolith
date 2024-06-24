package pl.szymanski.springfrontend.forms;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class CategoryForm {

  @NotBlank(message = "{categories.add.form.name.blank}")
  private String name;

  @Digits(integer = 2, fraction = 0, message = "{categories.add.form.daysOfRent.invalid}")
  @Min(value = 1, message = "{categories.add.form.daysOfRent.tolow}")
  private int daysOfRent;

  private Boolean continuationPossible;

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

  public Boolean getContinuationPossible() {
    return continuationPossible;
  }

  public void setContinuationPossible(Boolean continuationPossible) {
    this.continuationPossible = continuationPossible;
  }
}
