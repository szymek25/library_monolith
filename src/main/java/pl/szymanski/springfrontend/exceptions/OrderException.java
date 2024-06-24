package pl.szymanski.springfrontend.exceptions;

public class OrderException extends Exception {

  public OrderException(String errorMessage) {
    super(errorMessage);
  }
}
