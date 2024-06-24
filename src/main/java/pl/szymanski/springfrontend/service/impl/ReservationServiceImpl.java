package pl.szymanski.springfrontend.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.dao.ReservationDao;
import pl.szymanski.springfrontend.enums.ReservationStatus;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.exceptions.ReservationException;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.model.Category;
import pl.szymanski.springfrontend.model.Department;
import pl.szymanski.springfrontend.model.Reservation;
import pl.szymanski.springfrontend.model.User;
import pl.szymanski.springfrontend.service.BookEntryService;
import pl.szymanski.springfrontend.service.EmailService;
import pl.szymanski.springfrontend.service.RentService;
import pl.szymanski.springfrontend.service.ReservationService;
import pl.szymanski.springfrontend.service.UserService;

@Service
public class ReservationServiceImpl implements ReservationService {

  @Autowired
  private BookEntryService bookEntryService;

  @Autowired
  private ReservationDao reservationDao;

  @Autowired
  private RentService rentService;

  @Autowired
  private UserService userService;

  @Autowired
  private EmailService emailService;

  @Override
  public Reservation createReservation(final int bookEntryId, final User user)
      throws BookException {
    BookEntry bookEntry = bookEntryService.getBookEntryById(bookEntryId);
    if (bookEntry == null) {
      throw new BookException(
          "Not Found book entry with id: " + bookEntryId);
    }

    return createReservation(user, bookEntry);
  }

  @Override
  public Page<Reservation> getAllPaginated(final Pageable pageable) {
    return reservationDao.findAll(pageable);
  }

  @Override
  public Page<Reservation> getAllReservationsForUser(final Pageable pageable, final User user) {
    return reservationDao.findByUserEmail(pageable, user.getEmail());
  }

  @Override
  public Reservation cancelReservation(final int reservationId) throws ReservationException {
    final Reservation reservation = reservationDao.findById(reservationId).orElseThrow(
        () -> new ReservationException("Not found reservation with id: " + reservationId));
    cancelReservation(reservation);

    return reservation;
  }

  protected void cancelReservation(final Reservation reservation) {
    final boolean reservationWasReadyForCollect = ReservationStatus.READY_FOR_COLLECT
        .equals(reservation.getStatus());
    reservation.setStatus(ReservationStatus.CANCELED);
    reservation.setQueueNo(null);
    reservation.setPredictedTimeForCollect(null);
    reservationDao.save(reservation);

    final BookEntry bookEntry = bookEntryService.getBookEntryById(reservation.getBookEntryId());
    final List<Reservation> reservations = getActiveReservations(bookEntry);
    if (CollectionUtils.isNotEmpty(reservations)) {
      updateReservationQueue(reservations, bookEntry);
    }
    Reservation newReadyForCollectReservation = null;
    if (reservationWasReadyForCollect) {
      newReadyForCollectReservation = markFirstReservationFromQueueAsReadyForCollect(reservations);
    }
    if (newReadyForCollectReservation != null) {
      reservations.remove(newReadyForCollectReservation);
    }
    sendNotificationsAboutChangedPredictedDate(reservations);
  }

  protected void sendNotificationsAboutChangedPredictedDate(
      final List<Reservation> reservationQueue) {
    new Thread(() -> {
      reservationQueue.forEach(
          reservation -> emailService.notifyAboutChangePredictedDateForCollect(reservation));
    }).start();
  }

  @Override
  public boolean checkIfCurrentUserHasActiveReservation(final BookEntry bookEntry) {
    final User user = userService.getCurrentUser();
    final List<Reservation> reservations = reservationDao
        .findByUserEmailAndBookEntryId(user.getEmail(), bookEntry.getBookEntry_id());
    return reservations.stream()
        .anyMatch(this::checkIfReservationIsActive);
  }

  @Override
  public boolean checkIfBookEntryHasActiveReservations(final BookEntry bookEntry) {
    final List<Reservation> reservations = reservationDao
        .findByBookEntryId(bookEntry.getBookEntry_id());
    return reservations.stream()
        .anyMatch(this::checkIfReservationIsActive);
  }

  @Override
  public List<Reservation> handleReservationsAfterEndRent(final BookEntry bookEntry) {
    final List<Reservation> activeReservations = getActiveReservations(bookEntry);
    updateReservationQueue(activeReservations, bookEntry);
    markFirstReservationFromQueueAsReadyForCollect(activeReservations);

    return activeReservations;
  }

  @Override
  public Reservation finishReservation(final int reservationId) throws ReservationException {
    final Reservation reservation = reservationDao.findById(reservationId)
        .orElseThrow(
            () -> new ReservationException("Not found reservation with id: " + reservationId));
    reservation.setStatus(ReservationStatus.PERFORMED);
    reservation.setQueueNo(null);
    reservation.setPredictedTimeForCollect(null);
    reservationDao.save(reservation);

    return reservation;
  }

  @Override
  public void updateReservationQueueForBookEntry(final BookEntry bookEntry) {
    final List<Reservation> activeReservations = getActiveReservations(bookEntry);
    updateReservationQueue(activeReservations, bookEntry);
    sendNotificationsAboutChangedPredictedDate(activeReservations);
  }

  @Override
  public void checkAllUnCollectedReservationsAndUpdateQueue() {
    final List<Reservation> reservationReadyForCollect = reservationDao
        .findByStatus(ReservationStatus.READY_FOR_COLLECT);
    LocalDateTime now = LocalDateTime.now();
    final List<Reservation> unCollectedReservations = reservationReadyForCollect.stream()
        .filter(reservation -> {
          final LocalDateTime timeFromReadyForCollect = reservation
              .getTimeFromReservationIsReadyForCollect().toLocalDateTime();

          return timeFromReadyForCollect
              .plusDays(ApplicationConstants.DAYS_FOR_COLLECT_BOOK_AFTER_RESERVATION).isBefore(now);
        })
        .collect(Collectors.toList());

    unCollectedReservations.forEach(reservation -> {
      cancelReservation(reservation);
      emailService.cancelReservationNotification(reservation);
    });
  }

  @Override
  public void handleReservationsAfterCancelOrder(final BookEntry bookEntry) {
    final List<Reservation> activeReservations = getActiveReservations(bookEntry);
    if(CollectionUtils.isNotEmpty(activeReservations)) {
      updateReservationQueue(activeReservations, bookEntry);
      markFirstReservationFromQueueAsReadyForCollect(activeReservations);
    }

  }

  protected Reservation markFirstReservationFromQueueAsReadyForCollect(
      final List<Reservation> reservationsQueue) {
    if (CollectionUtils.isNotEmpty(reservationsQueue)) {
      final Reservation reservation = reservationsQueue.stream()
          .sorted(Comparator.comparing(Reservation::getReservationDate))
          .findFirst()
          .get();
      reservation.setStatus(ReservationStatus.READY_FOR_COLLECT);
      reservation
          .setTimeFromReservationIsReadyForCollect(new Timestamp(System.currentTimeMillis()));

      reservationDao.save(reservation);
      new Thread(
          () -> emailService.notifyFirstUserFromReservationQueueAboutReadyToCollect(reservation))
          .start();

      return reservation;
    }

    return null;
  }

  protected boolean checkIfReservationIsActive(final Reservation reservation) {
    final ReservationStatus status = reservation.getStatus();
    return ReservationStatus.IN_PROGRESS.equals(status) || ReservationStatus.READY_FOR_COLLECT
        .equals(status);
  }

  protected Reservation createReservation(final User user, final BookEntry bookEntry) {
    final Reservation reservation = new Reservation();
    reservation.setReservationDate(new Timestamp(System.currentTimeMillis()));
    reservation.setBookEntryId(bookEntry.getBookEntry_id());
    reservation.setUserEmail(user.getEmail());
    reservation.setBookInventoryNumber(bookEntry.getInventoryNumber());
    reservation.setBookTitle(bookEntry.getBook().getTitle());
    reservation.setStatus(ReservationStatus.IN_PROGRESS);
    final Department department = bookEntry.getDepartment();
    if (department != null) {
      reservation.setDepartmentName(department.getName());
    }

    setQueueNo(bookEntry, reservation);
    setPredictedDateForCollect(reservation, bookEntry, reservation.getQueueNo());
    reservationDao.save(reservation);
    return reservation;
  }

  protected void setQueueNo(final BookEntry bookEntry, final Reservation reservation) {
    final List<Reservation> reservations = getActiveReservations(bookEntry);
    if (CollectionUtils.isNotEmpty(reservations)) {
      reservation.setQueueNo(reservations.size() + 1);
    } else {
      reservation.setQueueNo(1);
    }
  }

  protected List<Reservation> getActiveReservations(final BookEntry bookEntry) {
    final List<Reservation> reservations = reservationDao
        .findByBookEntryId(bookEntry.getBookEntry_id());
    if (CollectionUtils.isNotEmpty(reservations)) {
      return reservations.stream()
          .filter(this::checkIfReservationIsActive)
          .collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }

  protected void updateReservationQueue(final List<Reservation> reservations,
      final BookEntry bookEntry) {
    final List<Reservation> sortedReservations = reservations.stream()
        .sorted(Comparator.comparing(Reservation::getReservationDate))
        .collect(Collectors.toList());

    int queueNo = 1;
    for (final Reservation reservation : sortedReservations) {
      reservation.setQueueNo(queueNo);
      setPredictedDateForCollect(reservation, bookEntry, queueNo);
      reservationDao.save(reservation);
      queueNo++;
    }
  }

  protected void setPredictedDateForCollect(final Reservation reservation,
      final BookEntry bookEntry, int queueNo) {
    final Category category = bookEntry.getBook().getCategory();
    final Date availabilityDateOfBook = rentService.getAvailabilityDateOfBook(bookEntry);
    int daysForCollect = 0;
    if (queueNo != 1) {
      daysForCollect = (queueNo - 1) * category.getDaysOfRent();
      daysForCollect +=
          (queueNo - 1) * ApplicationConstants.DAYS_FOR_COLLECT_BOOK_AFTER_RESERVATION;
    }

    if (availabilityDateOfBook != null) {
      final LocalDate predictedTimeForCollect =
          daysForCollect != 0 ? availabilityDateOfBook.toLocalDate()
              .plusDays(daysForCollect) : availabilityDateOfBook.toLocalDate().plusDays(1);
      reservation.setPredictedTimeForCollect(java.sql.Date.valueOf(predictedTimeForCollect));
    } else {
      reservation.setPredictedTimeForCollect(null);
    }
  }

}
