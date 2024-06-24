package pl.szymanski.springfrontend.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.szymanski.springfrontend.dtos.BookDTO;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.forms.BookForm;

public interface BookFacade {

  void addNewBook(BookForm bookForm) throws BookException;

  Page<BookDTO> getPaginatedBooks(PageRequest pageRequest, Integer[] filters);

  BookDTO getBookById(int id);

  void updateBook(int id, BookForm bookForm) throws BookException;

  String deleteBook(int id);

}
