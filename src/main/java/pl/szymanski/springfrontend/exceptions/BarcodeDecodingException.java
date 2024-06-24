package pl.szymanski.springfrontend.exceptions;

public class BarcodeDecodingException extends Exception {

  public BarcodeDecodingException(final String errorMessage) {
    super(errorMessage);
  }

}
