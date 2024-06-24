package pl.szymanski.springfrontend;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.dao.ReservationDao;
import pl.szymanski.springfrontend.enums.ReservationStatus;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.model.Book;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.model.Category;
import pl.szymanski.springfrontend.model.Reservation;
import pl.szymanski.springfrontend.model.User;
import pl.szymanski.springfrontend.service.EmailService;
import pl.szymanski.springfrontend.service.RentService;
import pl.szymanski.springfrontend.service.impl.BookEntryServiceImpl;
import pl.szymanski.springfrontend.service.impl.ReservationServiceImpl;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ReservationServiceTest {

  private final static int BOOK_ENTRY_TEST_ID = 1;
  private final static int DAYS_AFTER_BOOK_WILL_BE_AVAILABLE = 15;
  private final static int CATEGORY_DAYS_OF_RENT = 30;

  @Mock
  private ReservationDao reservationDao;

  @Mock
  private BookEntryServiceImpl bookEntryService;

  @Mock
  private RentService rentService;

  @Mock
  private EmailService emailService;

  @InjectMocks
  private ReservationServiceImpl reservationService;

  @Before
  public void setUp() {
    when(reservationDao.findByBookEntryId(BOOK_ENTRY_TEST_ID)).thenReturn(getStubReservations());
    when(bookEntryService.getBookEntryById(BOOK_ENTRY_TEST_ID)).thenReturn(getStubBookEntry());

    when(rentService.getAvailabilityDateOfBook(any()))
        .thenReturn(Date.valueOf(LocalDate.now().plusDays(DAYS_AFTER_BOOK_WILL_BE_AVAILABLE)));

  }

  private BookEntry getStubBookEntry() {
    final BookEntry bookEntry = new BookEntry();
    bookEntry.setBookEntry_id(BOOK_ENTRY_TEST_ID);
    final Book book = new Book();
    final Category category = new Category();
    category.setDaysOfRent(CATEGORY_DAYS_OF_RENT);
    book.setCategory(category);
    bookEntry.setBook(book);

    return bookEntry;
  }

  @Test
  public void testSettingQueueNo() throws BookException {
    final Reservation reservation = reservationService
        .createReservation(BOOK_ENTRY_TEST_ID, new User());

    Assert.assertEquals(3, reservation.getQueueNo().intValue());
  }

  @Test
  public void testSetPredictedTimeForCollect() throws BookException {
    final Reservation reservation = reservationService
        .createReservation(BOOK_ENTRY_TEST_ID, new User());

    final Date expectedDate = Date.valueOf(LocalDate.now()
        .plusDays(DAYS_AFTER_BOOK_WILL_BE_AVAILABLE)
        .plusDays(2 * CATEGORY_DAYS_OF_RENT)
        .plusDays(2 * ApplicationConstants.DAYS_FOR_COLLECT_BOOK_AFTER_RESERVATION));
    Assert.assertEquals(expectedDate, reservation.getPredictedTimeForCollect());
  }

  @Test
  public void testReservationQueue(){
    final List<Reservation> reservations = reservationService.handleReservationsAfterEndRent(getStubBookEntry());
    final Reservation reservationShouldBeReady = reservations.get(0);

    Assert.assertEquals(ReservationStatus.READY_FOR_COLLECT, reservationShouldBeReady.getStatus());
    verify(emailService).notifyFirstUserFromReservationQueueAboutReadyToCollect(reservationShouldBeReady);
    Assert.assertEquals(2,reservations.size());


  }

  private List<Reservation> getStubReservations() {
    final Reservation reservation1 = new Reservation();
    reservation1.setReservationId(1);
    reservation1.setStatus(ReservationStatus.IN_PROGRESS);
    reservation1.setQueueNo(1);
    reservation1.setReservationDate(new Timestamp(new java.util.Date(110,01,02,15,13).getTime()));

    final Reservation reservation2 = new Reservation();
    reservation2.setReservationId(2);
    reservation2.setStatus(ReservationStatus.CANCELED);
    reservation2.setReservationDate(new Timestamp(new java.util.Date(110,01,04,11,12).getTime()));

    final Reservation reservation3 = new Reservation();
    reservation3.setReservationId(3);
    reservation3.setStatus(ReservationStatus.IN_PROGRESS);
    reservation3.setQueueNo(2);
    reservation3.setReservationDate(new Timestamp(new java.util.Date(110,01,06,14,13).getTime()));
    return Arrays.asList(reservation1, reservation2, reservation3);
  }

}
