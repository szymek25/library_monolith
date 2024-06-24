package pl.szymanski.springfrontend.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.service.ReservationService;

@Component
public class ReservationsTask {

  @Autowired
  private ReservationService reservationService;

  @Scheduled(fixedDelay = 1000000)
  public void checkAllUnCollectedReservations() {
    reservationService.checkAllUnCollectedReservationsAndUpdateQueue();
  }
}
