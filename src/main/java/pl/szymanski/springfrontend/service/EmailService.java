package pl.szymanski.springfrontend.service;

import pl.szymanski.springfrontend.model.BookOrder;
import pl.szymanski.springfrontend.model.Reservation;

public interface EmailService {

  void notifyFirstUserFromReservationQueueAboutReadyToCollect(Reservation reservation);

  void notifyAboutChangePredictedDateForCollect(Reservation reservation);

  void cancelReservationNotification(Reservation reservation);

  void notifyAboutOrderIsReadyForCollect(BookOrder bookOrder);

  void cancelOrderNotification(BookOrder bookOrder);
}
