package pl.szymanski.springfrontend.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.szymanski.springfrontend.model.Rent;

@Repository
public interface RentDao extends CrudRepository<Rent, Integer> {

  Page<Rent> findAll(Pageable pageable);

  Page<Rent> findByUserEmail(Pageable pageable, String userEmail);

  List<Rent> findByBookEntryId(int bookId);

  Page<Rent> findByBookEntryId(Pageable pageable, int bookId);
}
