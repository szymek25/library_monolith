package pl.szymanski.springfrontend.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.szymanski.springfrontend.constants.ResponseCodes;
import pl.szymanski.springfrontend.dao.RentDao;
import pl.szymanski.springfrontend.enums.OrderStatus;
import pl.szymanski.springfrontend.exceptions.ProlongationException;
import pl.szymanski.springfrontend.exceptions.RentException;
import pl.szymanski.springfrontend.model.Book;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.model.BookOrder;
import pl.szymanski.springfrontend.model.Category;
import pl.szymanski.springfrontend.model.Department;
import pl.szymanski.springfrontend.model.Rent;
import pl.szymanski.springfrontend.model.User;
import pl.szymanski.springfrontend.service.BookEntryService;
import pl.szymanski.springfrontend.service.BookService;
import pl.szymanski.springfrontend.service.OrderService;
import pl.szymanski.springfrontend.service.RentService;
import pl.szymanski.springfrontend.service.ReservationService;
import pl.szymanski.springfrontend.service.UserService;


@Service
public class RentServiceImpl implements RentService {

  public static final String EUROPE_WARSAW = "Europe/Warsaw";


  @Autowired
  private UserService userService;

  @Autowired
  private BookService bookService;

  @Autowired
  private BookEntryService bookEntryService;

  @Autowired
  private RentDao rentDao;

  @Autowired
  private OrderService orderService;

  @Autowired
  private ReservationService reservationService;

  @Override
  public Rent createNewRent(final BookEntry bookEntry, final User user,
      final boolean collectReservation) throws RentException {

    final Book book = bookEntry.getBook();
    if (book == null) {
      throw new RentException(
          "Book entry with id:" + bookEntry.getBookEntry_id() + " is not assigned to book");
    }
    Category bookCategory = book.getCategory();
    if (bookCategory == null) {
      throw new RentException("Book with id: " + book.getId() + " is not assigned to category");
    }
    if (!collectReservation && isBookRented(bookEntry)) {
      throw new RentException("BookEntry: " + bookEntry.getBookEntry_id() + " is already rented");
    }
    return createNewRent(user, bookEntry, book, bookCategory);
  }

  public boolean isBookRented(final BookEntry book) {
    if (book == null) {
      return false;
    }
    List<BookOrder> orders = orderService.getBookOrdersForBookEntry(book);
    if ((orders != null && orderService.hasActiveOrder(orders)) || reservationService
        .checkIfBookEntryHasActiveReservations(book)) {
      return true;
    }
    List<Rent> rents = rentDao.findByBookEntryId(book.getBookEntry_id());
    return rents.stream()
        .filter(rent -> rent.getReturnDate() == null)
        .count() > 0;
  }

  @Override
  public Rent performProlongation(final int rentId) throws RentException, ProlongationException {
    Rent rent;
    Optional<Rent> rentOptional = rentDao.findById(rentId);
    if (rentOptional.isPresent()) {
      rent = rentOptional.get();
    } else {
      throw new RentException("Rent with id: " + rentId + " no exsists");
    }
    final BookEntry bookEntry = bookEntryService.getBookEntryById(rent.getBookEntryId());
    if (bookEntry == null) {
      throw new RentException(
          "Book entry with id:" + bookEntry.getBookEntry_id() + " is not exsists");
    }
    final Book book = bookEntry.getBook();
    if (book == null) {
      throw new RentException(
          "Book entry with id:" + bookEntry.getBookEntry_id() + " is not assigned to book");
    }
    final Category bookCategory = book.getCategory();
    if (bookCategory == null) {
      throw new RentException("Book with id: " + book.getId() + " is not assigned to category");
    }
    final boolean prolongationPerformed = BooleanUtils.toBoolean(rent.getProlongationPerformed());
    if (prolongationPerformed) {
      throw new ProlongationException(
          "Prolongation was arleady performed for rent: "
              + rentId);
    }
    if (!bookCategory.isContinuationPossible()) {
      throw new RentException("Prolongation is not possible for category: " + bookCategory.getId());
    }
    final int categoryDaysOfRent = bookCategory.getDaysOfRent();
    final Date endDate = rent.getEndDate();
    final LocalDate endDateAfterProlongation = endDate.toLocalDate().plusDays(categoryDaysOfRent);
    rent.setEndDate(java.sql.Date.valueOf(endDateAfterProlongation));
    rent.setProlongationPerformed(true);
    rentDao.save(rent);

    reservationService.updateReservationQueueForBookEntry(bookEntry);

    return rent;
  }

  @Override
  public Page<Rent> getAllPaginated(Pageable pageable) {
    return rentDao.findAll(pageable);
  }

  @Override
  public String endRent(int rentId, String physicalState) {
    Rent rent;
    Optional<Rent> rentOptional = rentDao.findById(rentId);
    if (rentOptional.isPresent()) {
      rent = rentOptional.get();
    } else {
      return ResponseCodes.RENT_DOES_NOT_EXSIST;
    }
    ZonedDateTime startTime = LocalDateTime.now().atZone(ZoneId.of(EUROPE_WARSAW));
    rent.setReturnDate(new Date(startTime.toInstant().toEpochMilli()));
    rentDao.save(rent);

    if (!StringUtils.isEmpty(physicalState)) {
      bookEntryService.updatePhysicalState(rent.getBookEntryId(), physicalState);
    }

    final BookEntry bookEntry = bookEntryService.getBookEntryById(rent.getBookEntryId());
    reservationService.handleReservationsAfterEndRent(bookEntry);
    return ResponseCodes.SUCCESS;
  }

  @Override
  public Page<Rent> getAllPaginatedForUser(Pageable pageable, int userId) {
    User user = userService.getUserById(userId);
    return rentDao.findByUserEmail(pageable, user.getEmail());
  }

  @Override
  public Page<Rent> getAllPaginatedForBookEntry(final Pageable pageable, final int bookEntryId) {
    return rentDao.findByBookEntryId(pageable, bookEntryId);
  }

  @Override
  public Date getAvailabilityDateOfBook(BookEntry book) {
    List<Rent> rents = rentDao.findByBookEntryId(book.getBookEntry_id());
    Optional<Rent> actualRent = rents.stream()
        .filter(rent -> rent.getReturnDate() == null)
        .findFirst();

    if (actualRent.isPresent()) {
      return actualRent.get().getEndDate();
    } else {
      return rents.stream()
          .sorted(Comparator.comparing(Rent::getStartDate).reversed())
          .map(Rent::getReturnDate)
          .findFirst()
          .orElseGet(null);
    }
  }

  private Rent createNewRent(final User user, final BookEntry bookEntry, final Book book,
      final Category bookCategory) {
    Rent newRent = new Rent();
    newRent.setUserEmail(user.getEmail());
    newRent.setBookIsbn(book.getIsbn());
    newRent.setBookTitle(book.getTitle());
    newRent.setBookEntryId(bookEntry.getBookEntry_id());
    newRent.setBookInventoryNumber(bookEntry.getInventoryNumber());
    newRent.setBookPhysicalState(bookEntry.getPhysicalState());
    ZonedDateTime startTime = LocalDateTime.now().atZone(ZoneId.of(EUROPE_WARSAW));
    newRent.setStartDate(new Date(startTime.toInstant().toEpochMilli()));
    ZonedDateTime endTime = LocalDateTime.now().plusDays(bookCategory.getDaysOfRent())
        .atZone(ZoneId.of(EUROPE_WARSAW));
    newRent.setEndDate(new Date(endTime.toInstant().toEpochMilli()));
    final Department department = bookEntry.getDepartment();
    if (department != null) {
      newRent.setDepartmentName(department.getName());
    }
    rentDao.save(newRent);

    return newRent;
  }

}
