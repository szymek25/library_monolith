package pl.szymanski.springfrontend.service;

import pl.szymanski.springfrontend.dtos.BookEntryDTO;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.model.BookEntry;

public interface BookEntryService {

  void addBookEntry(BookEntryDTO bookEntryDTO) throws BookException;

  BookEntry getBookEntryById(int id);

  BookEntry editBookEntry(BookEntryDTO bookEntryDTO);

  void removeBookEntry(int id);

  void updatePhysicalState(int id, String physicalState);
}
