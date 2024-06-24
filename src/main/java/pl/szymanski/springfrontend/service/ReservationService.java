package pl.szymanski.springfrontend.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.exceptions.ReservationException;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.model.Reservation;
import pl.szymanski.springfrontend.model.User;

public interface ReservationService {

  Reservation createReservation(int bookEntryId, User user) throws BookException;

  Page<Reservation> getAllPaginated(Pageable pageable);

  Page<Reservation> getAllReservationsForUser(Pageable pageable, User user);

  Reservation cancelReservation(int reservationId) throws ReservationException;

  boolean checkIfCurrentUserHasActiveReservation(BookEntry bookEntry);

  boolean checkIfBookEntryHasActiveReservations(BookEntry bookEntry);

  List<Reservation> handleReservationsAfterEndRent(BookEntry bookEntry);

  Reservation finishReservation(int reservationId) throws ReservationException;

  void updateReservationQueueForBookEntry(BookEntry bookEntry);

  void checkAllUnCollectedReservationsAndUpdateQueue();

  void handleReservationsAfterCancelOrder(BookEntry bookEntry);
}
