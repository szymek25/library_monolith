package pl.szymanski.springfrontend.service;

import java.sql.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.szymanski.springfrontend.exceptions.ProlongationException;
import pl.szymanski.springfrontend.exceptions.RentException;
import pl.szymanski.springfrontend.model.Book;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.model.Rent;
import pl.szymanski.springfrontend.model.User;

public interface RentService {

  Rent createNewRent(BookEntry bookEntry, User user, boolean collectReservation) throws RentException;

  Page<Rent> getAllPaginated(Pageable pageable);

  String endRent(int rentId, String physicalState);

  Page<Rent> getAllPaginatedForUser(Pageable pageable, int userId);

  Page<Rent> getAllPaginatedForBookEntry(Pageable pageable, int bookEntryId);

  Date getAvailabilityDateOfBook(BookEntry book);

  boolean isBookRented(final BookEntry book);

  Rent performProlongation(int rentId) throws RentException, ProlongationException;
}
