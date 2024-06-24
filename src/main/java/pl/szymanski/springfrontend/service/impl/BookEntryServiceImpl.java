package pl.szymanski.springfrontend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.szymanski.springfrontend.dao.BookEntryDao;
import pl.szymanski.springfrontend.dtos.BookEntryDTO;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.model.Book;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.model.Department;
import pl.szymanski.springfrontend.service.BookEntryService;
import pl.szymanski.springfrontend.service.BookService;
import pl.szymanski.springfrontend.service.UserService;

@Service
public class BookEntryServiceImpl implements BookEntryService {

  @Autowired
  private BookEntryDao bookEntryDao;

  @Autowired
  private BookService bookService;

  @Autowired
  private UserService userService;

  @Override
  public void addBookEntry(BookEntryDTO bookEntryDTO) throws BookException {
    final BookEntry bookEntry = new BookEntry();
    int bookId = bookEntryDTO.getBookId();
    final Book book = bookService.getBookById(bookId);
    if (book == null) {
      throw new BookException("Book with id: " + bookId + " doesn`t exists");
    }
    bookEntry.setBook(book);
    bookEntry.setInventoryNumber(bookEntryDTO.getInventoryNumber());
    bookEntry.setPhysicalState(bookEntryDTO.getPhysicalState());
    bookEntry.setSignature(bookEntryDTO.getSignature());
    final Department department = userService.getCurrentUser().getDepartment();
    bookEntry.setDepartment(department);

    bookEntryDao.save(bookEntry);
  }

  @Override
  public BookEntry getBookEntryById(int id) {
    return bookEntryDao.findById(id).orElse(null);
  }

  @Override
  public BookEntry editBookEntry(BookEntryDTO bookEntryDTO) {
    final BookEntry bookEntry = getBookEntryById(bookEntryDTO.getId());
    if (bookEntry != null) {
      bookEntry.setSignature(bookEntryDTO.getSignature());
      bookEntry.setPhysicalState(bookEntryDTO.getPhysicalState());
      bookEntry.setInventoryNumber(bookEntryDTO.getInventoryNumber());

      bookEntryDao.save(bookEntry);
      return bookEntry;
    }
    return null;
  }

  @Override
  public void removeBookEntry(int id) {
    bookEntryDao.deleteById(id);
  }

  @Override
  public void updatePhysicalState(int id, String physicalState) {
    final BookEntry bookEntry = getBookEntryById(id);
    bookEntry.setPhysicalState(physicalState);
    bookEntryDao.save(bookEntry);
  }
}
