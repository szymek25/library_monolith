package pl.szymanski.springfrontend.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.szymanski.springfrontend.constants.ExceptionCodes;
import pl.szymanski.springfrontend.constants.ResponseCodes;
import pl.szymanski.springfrontend.dao.BookDao;
import pl.szymanski.springfrontend.dtos.BookDTO;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.model.Book;
import pl.szymanski.springfrontend.model.Category;
import pl.szymanski.springfrontend.service.BookService;
import pl.szymanski.springfrontend.service.CategoryService;

@Service
public class BookServiceImpl implements BookService {

  @Autowired
  private CategoryService categoryService;
  @Autowired
  private BookDao bookDao;

  @Override
  public void addNewBook(BookDTO bookDTO) throws BookException {
    if (bookDao.existsByIsbn(bookDTO.getIsbn())) {
      throw new BookException(ExceptionCodes.BOOK_ARLEADY_EXSIST);
    }
    Book book = new Book();
    try {
      convertBookDTO(bookDTO, book);
      setCategory(bookDTO, book);
      bookDao.save(book);
    } catch (Exception e) {
      throw new BookException(ExceptionCodes.GENERAL_SERVER_ERROR);
    }
  }

  @Override
  public Page<Book> getAllPaginated(Pageable pageable, Integer[] filters) {
    return bookDao.findByCategories(filters, pageable);
  }

  @Override
  public Page<Book> getAllPaginated(Pageable pageable) {
    return bookDao.findAll(pageable);
  }

  @Override
  public Book getBookById(int id) {
    Optional<Book> book = bookDao.findById(id);
    return book.isPresent() ? book.get() : null;
  }

  @Override
  public void updateBook(int id, BookDTO bookDTO) throws BookException {
    Book book = getBookById(id);
    if (book == null) {
      throw new BookException(ExceptionCodes.BOOK_DOES_NOT_EXSIST);
    }

    if (!book.getIsbn().equalsIgnoreCase(bookDTO.getIsbn()) && bookDao
        .existsByIsbn(bookDTO.getIsbn())) {
      throw new BookException(ExceptionCodes.BOOK_ARLEADY_EXSIST);
    }

    try {
      updateBook(bookDTO, book);
      bookDao.save(book);
    } catch (Exception e) {
      throw new BookException(ExceptionCodes.GENERAL_SERVER_ERROR);
    }
  }

  @Override
  public String deleteBook(int id) {
    bookDao.deleteById(id);

    return ResponseCodes.SUCCESS;
  }

  @Override
  public List<Book> getByBooksCategory(final Category category) {
    return bookDao.findByCategory(category);
  }

  private void updateBook(BookDTO bookDTO, Book book) throws Exception {
    convertBookDTO(bookDTO, book);
    setCategory(bookDTO, book);
  }

  private void setCategory(BookDTO bookDTO, Book book) throws Exception {
    Category category = categoryService.getCategoryById(bookDTO.getCategoryId());
    if (category == null) {
      throw new Exception("Didn`t find category with id: " + bookDTO.getCategoryId());
    }
    book.setCategory(category);
  }

  private void convertBookDTO(BookDTO bookDTO, Book book) {
    book.setIsbn(bookDTO.getIsbn());
    book.setTitle(bookDTO.getTitle());
    book.setAuthor(bookDTO.getAuthor());
    book.setPublicationYear(bookDTO.getPublicationYear());
    book.setPublisher(bookDTO.getPublisher());
  }
}
