package pl.szymanski.springfrontend.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.szymanski.springfrontend.dtos.ReservationDTO;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.exceptions.RentException;
import pl.szymanski.springfrontend.exceptions.ReservationException;
import pl.szymanski.springfrontend.facade.ReservationFacade;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.model.Reservation;
import pl.szymanski.springfrontend.model.User;
import pl.szymanski.springfrontend.service.BookEntryService;
import pl.szymanski.springfrontend.service.DepartmentService;
import pl.szymanski.springfrontend.service.RentService;
import pl.szymanski.springfrontend.service.ReservationService;
import pl.szymanski.springfrontend.service.UserService;

@Component
public class ReservationFacadeImpl implements ReservationFacade {

  @Autowired
  private UserService userService;

  @Autowired
  private ReservationService reservationService;

  @Autowired
  private BookEntryService bookEntryService;

  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private RentService rentService;

  @Override
  public void createReservation(final int bookEntryId) throws BookException {
    final User currentUser = userService.getCurrentUser();
    reservationService
        .createReservation(bookEntryId, currentUser);
  }

  @Override
  public Page<ReservationDTO> getAllPaginated(final Pageable pageable) {
    final Page<Reservation> allPaginated = reservationService.getAllPaginated(pageable);
    return convertToPageWithReservationDTO(allPaginated);
  }

  @Override
  public Page<ReservationDTO> getReservationsForCurrentUser(final Pageable pageable) {
    final User currentUser = userService.getCurrentUser();
    final Page<Reservation> allReservationsForUser = reservationService
        .getAllReservationsForUser(pageable, currentUser);
    return convertToPageWithReservationDTO(allReservationsForUser);
  }

  @Override
  public Page<ReservationDTO> getAllReservationForUser(final Pageable pageable, final int userId) {
    final User user = userService.getUserById(userId);
    final Page<Reservation> allReservationsForUser = reservationService
        .getAllReservationsForUser(pageable, user);
    return convertToPageWithReservationDTO(allReservationsForUser);
  }

  @Override
  public void cancelReservation(final int reservationId) throws ReservationException {
    reservationService.cancelReservation(reservationId);
  }

  @Override
  @Transactional
  public void finishReservation(int reservationId) throws ReservationException, RentException {
    final Reservation reservation = reservationService.finishReservation(reservationId);
    final User user = userService.getUserByEmail(reservation.getUserEmail());
    final BookEntry bookEntry = bookEntryService.getBookEntryById(reservation.getBookEntryId());
    if (user == null || bookEntry == null) {
      throw new ReservationException("Invalid input data, user or bookEntry was null");
    }
    rentService.createNewRent(bookEntry, user, true);
    reservationService.updateReservationQueueForBookEntry(bookEntry);

  }

  private Page<ReservationDTO> convertToPageWithReservationDTO(
      final Page<Reservation> reservations) {
    return reservations != null ? reservations.map(this::createReservationDto) : null;
  }

  protected ReservationDTO createReservationDto(final Reservation reservation) {
    final ReservationDTO reservationDTO = new ReservationDTO(reservation);
    final BookEntry bookEntry = bookEntryService.getBookEntryById(reservation.getBookEntryId());
    if (bookEntry != null) {
      reservationDTO.setCurrentEmployeeHasPermissionToBook(
          departmentService.checkIfCurrentUserHasPermissionToBook(bookEntry));
    }

    return reservationDTO;
  }
}
