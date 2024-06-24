package pl.szymanski.springfrontend.facade.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.dtos.BookDTO;
import pl.szymanski.springfrontend.dtos.BookEntryDTO;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.facade.BookEntryFacade;
import pl.szymanski.springfrontend.facade.BookFacade;
import pl.szymanski.springfrontend.forms.BookForm;
import pl.szymanski.springfrontend.model.Book;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.service.BookService;
import pl.szymanski.springfrontend.service.DepartmentService;
import pl.szymanski.springfrontend.service.OrderService;
import pl.szymanski.springfrontend.service.RentService;
import pl.szymanski.springfrontend.service.ReservationService;
import pl.szymanski.springfrontend.service.UserService;

@Component
public class BookFacadeImpl implements BookFacade {

  @Autowired
  private BookService bookService;

  @Autowired
  private RentService rentService;

  @Autowired
  private BookEntryFacade bookEntryFacade;

  @Autowired
  private UserService userService;

  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private ReservationService reservationService;

  @Autowired
  private OrderService orderService;

  @Override
  public void addNewBook(BookForm bookForm) throws BookException {
    final BookDTO bookDTO = new BookDTO();
    bookDTO.setIsbn(bookForm.getIsbn());
    bookDTO.setCategoryId(Integer.parseInt(bookForm.getCategoryId()));
    bookDTO.setTitle(bookForm.getTitle());
    bookDTO.setAuthor(bookForm.getAuthor());
    bookDTO.setPublicationYear(bookForm.getPublicationYear());
    bookDTO.setPublisher(bookForm.getPublisher());

    bookService.addNewBook(bookDTO);
  }

  @Override
  public Page<BookDTO> getPaginatedBooks(PageRequest pageRequest, Integer[] filters) {
    if (filters != null && filters.length > 0) {
      return convertToPageBookDTO(bookService.getAllPaginated(pageRequest, filters));
    } else {
      return convertToPageBookDTO(bookService.getAllPaginated(pageRequest));
    }
  }

  @Override
  public BookDTO getBookById(int id) {
    final Book book = bookService.getBookById(id);
    if (book != null) {
      final BookDTO bookDTO = new BookDTO(book);
      createBookEntriesDto(book, bookDTO);
      bookDTO.getEntries().forEach(entry -> {
        entry.setBookRented(bookEntryFacade.isBookRented(entry));
      });
      return bookDTO;
    }
    return null;
  }

  @Override
  public void updateBook(final int id, final BookForm bookForm) throws BookException {
    final BookDTO bookDTO = new BookDTO();
    bookDTO.setIsbn(bookForm.getIsbn());
    bookDTO.setTitle(bookForm.getTitle());
    bookDTO.setAuthor(bookForm.getAuthor());
    bookDTO.setPublicationYear(bookForm.getPublicationYear());
    bookDTO.setPublisher(bookForm.getPublisher());
    bookDTO.setCategoryId(Integer.parseInt(bookForm.getCategoryId()));
    bookService.updateBook(id, bookDTO);
  }

  @Override
  public String deleteBook(int id) {
    return bookService.deleteBook(id);
  }

  private Page<BookDTO> convertToPageBookDTO(Page<Book> books) {
    return books.map(this::convertToBookDTO);
  }

  private BookDTO convertToBookDTO(final Book book) {
    final BookDTO bookDTO = new BookDTO(book);
    createBookEntriesDto(book, bookDTO);

    return bookDTO;
  }

  private void createBookEntriesDto(final Book book, final BookDTO bookDTO) {
    final List<BookEntryDTO> entries = book.getEntries().stream()
        .map(entry -> createBookEntryDto(entry))
        .collect(Collectors.toList());
    bookDTO.setEntries(entries);
  }

  private BookEntryDTO createBookEntryDto(final BookEntry entry) {
    final BookEntryDTO entryDTO = new BookEntryDTO(entry);
    entryDTO.setCurrentEmployeeHasPermissionToBook(
        departmentService.checkIfCurrentUserHasPermissionToBook(entry));
    entryDTO.setUserReservedBook(reservationService.checkIfCurrentUserHasActiveReservation(entry));
    entryDTO.setUserOrderedBook(orderService.checkIfCurrentUserHasActiveOrder(entry));
    return entryDTO;
  }

}
