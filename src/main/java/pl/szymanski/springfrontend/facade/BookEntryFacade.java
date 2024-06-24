package pl.szymanski.springfrontend.facade;

import pl.szymanski.springfrontend.dtos.BookEntryDTO;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.forms.BookEntryForm;
import pl.szymanski.springfrontend.forms.EditBookEntryForm;

public interface BookEntryFacade {

  void addBookEntry(BookEntryForm form) throws BookException;

  BookEntryDTO editBookEntry(EditBookEntryForm form);

  void removeBookEntry(int id);

  BookEntryDTO getBookEntryById(int id);

  boolean isBookRented(BookEntryDTO bookEntryDTO);
}
