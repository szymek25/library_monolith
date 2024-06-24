package pl.szymanski.springfrontend.exceptions;

import pl.szymanski.springfrontend.constants.ExceptionCodes;

public class ReservationException extends Exception {

  public ReservationException(String msg){
    super(msg);
  }
}
