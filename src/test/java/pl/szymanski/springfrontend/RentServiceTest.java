package pl.szymanski.springfrontend;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import pl.szymanski.springfrontend.dao.RentDao;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.model.Rent;
import pl.szymanski.springfrontend.service.OrderService;
import pl.szymanski.springfrontend.service.ReservationService;
import pl.szymanski.springfrontend.service.impl.RentServiceImpl;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class RentServiceTest {

  @Mock
  private RentDao rentDao;

  @Mock
  private OrderService orderService;

  @Mock
  private ReservationService reservationService;

  @InjectMocks
  private RentServiceImpl rentService;

  @Test
  public void testAvailabilityDateIfActualRentIsPresent() {
    when(rentDao.findByBookEntryId(anyInt())).thenReturn(getStubRentsWithActualRent());
    final Date availabilityDateOfBook = rentService.getAvailabilityDateOfBook(new BookEntry());

    Assert.assertEquals(new Date(120, 02, 25), availabilityDateOfBook);
  }


  @Test
  public void testAvailabilityDateIfActualRentIsNotPresent() {
    when(rentDao.findByBookEntryId(anyInt())).thenReturn(getStubRentsWithoutActualRent());
    final Date availabilityDateOfBook = rentService.getAvailabilityDateOfBook(new BookEntry());

    Assert.assertEquals(new Date(120, 02, 20), availabilityDateOfBook);
  }

  @Test
  public void testIfBookIsRented() {
    final BookEntry bookEntry = new BookEntry();
    when(orderService.getBookOrdersForBookEntry(any())).thenReturn(Collections.emptyList());
    when(reservationService.checkIfBookEntryHasActiveReservations(any())).thenReturn(false);
    when(rentDao.findByBookEntryId(anyInt())).thenReturn(getStubRentsWithActualRent());

    Assert.assertEquals(true, rentService.isBookRented(bookEntry));

    when(rentDao.findByBookEntryId(anyInt())).thenReturn(getStubRentsWithoutActualRent());
    Assert.assertEquals(false, rentService.isBookRented(bookEntry));
  }

  private List<Rent> getStubRentsWithActualRent() {
    List<Rent> rents = new ArrayList<>();
    final Rent rent1 = new Rent();
    rent1.setEndDate(new Date(120, 01, 25));
    rent1.setReturnDate(new Date(120, 01, 15));
    rents.add(rent1);

    final Rent rent2 = new Rent();
    rent2.setEndDate(new Date(120, 02, 05));
    rent2.setReturnDate(new Date(120, 01, 26));
    rents.add(rent2);

    final Rent rent3 = new Rent();
    rent3.setEndDate(new Date(120, 02, 25));
    rents.add(rent3);

    return rents;
  }


  private List<Rent> getStubRentsWithoutActualRent() {
    List<Rent> rents = new ArrayList<>();
    final Rent rent1 = new Rent();
    rent1.setStartDate(new Date(120, 01, 15));
    rent1.setEndDate(new Date(120, 01, 25));
    rent1.setReturnDate(new Date(120, 01, 15));
    rents.add(rent1);

    final Rent rent2 = new Rent();
    rent2.setStartDate(new Date(120, 01, 26));
    rent2.setEndDate(new Date(120, 02, 05));
    rent2.setReturnDate(new Date(120, 01, 26));
    rents.add(rent2);

    final Rent rent3 = new Rent();
    rent3.setStartDate(new Date(120, 02, 15));
    rent3.setEndDate(new Date(120, 02, 25));
    rent3.setReturnDate(new Date(120, 02, 20));
    rents.add(rent3);

    return rents;
  }
}
