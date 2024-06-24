package pl.szymanski.springfrontend.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.szymanski.springfrontend.dtos.ReservationDTO;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.exceptions.RentException;
import pl.szymanski.springfrontend.exceptions.ReservationException;

public interface ReservationFacade {

  void createReservation(int bookEntryId) throws BookException;

  Page<ReservationDTO> getAllPaginated(Pageable pageable);

  Page<ReservationDTO> getReservationsForCurrentUser(Pageable pageable);

  Page<ReservationDTO> getAllReservationForUser(Pageable pageable, int userId);

  void cancelReservation(int reservationId) throws ReservationException;

  void finishReservation(int reservationId) throws ReservationException, RentException;
}
