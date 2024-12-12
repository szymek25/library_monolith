package pl.szymanski.springfrontend.exceptions;

public class DuplicatedUserException extends Exception{

	public DuplicatedUserException(String errorMessage) {
		super(errorMessage);
	}

	public DuplicatedUserException() {
		super();
	}
}
