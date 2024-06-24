package pl.szymanski.springfrontend.service;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.szymanski.springfrontend.dtos.BookDTO;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.model.Book;
import pl.szymanski.springfrontend.model.Category;

public interface BookService {

  void addNewBook(BookDTO bookDTO) throws BookException;

  Page<Book> getAllPaginated(Pageable pageable);

  Page<Book> getAllPaginated(Pageable pageable, Integer[] filters);

  Book getBookById(int id);

  void updateBook(int id, BookDTO bookDTO) throws BookException;

  String deleteBook(int id);

  List<Book> getByBooksCategory(Category category);
}
