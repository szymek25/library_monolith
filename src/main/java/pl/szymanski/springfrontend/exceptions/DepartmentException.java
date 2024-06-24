package pl.szymanski.springfrontend.exceptions;

public class DepartmentException extends Exception {

  public DepartmentException(String errorMessage) {
    super(errorMessage);
  }

  public DepartmentException() {
    super();
  }
}
