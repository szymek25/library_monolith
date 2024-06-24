package pl.szymanski.springfrontend.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import pl.szymanski.springfrontend.enums.ReservationStatus;
import pl.szymanski.springfrontend.model.Reservation;

public interface ReservationDao extends CrudRepository<Reservation, Integer> {

  Page<Reservation> findAll(Pageable pageable);

  Page<Reservation> findByUserEmail(Pageable pageable, String userEmail);

  List<Reservation> findByUserEmailAndBookEntryId(String userEmail, int bookEntryId);

  List<Reservation> findByBookEntryId(int bookEntryId);

  List<Reservation> findByStatus(ReservationStatus status);

}
