package pl.szymanski.springfrontend.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.szymanski.springfrontend.enums.OrderStatus;
import pl.szymanski.springfrontend.model.BookOrder;

@Repository
public interface OrderDao extends CrudRepository<BookOrder, Integer> {

  Page<BookOrder> findAll(Pageable pageable);

  Page<BookOrder> findByUserEmail(Pageable pageable, String userEmail);

  List<BookOrder> findByBookEntryId(int id);

  List<BookOrder> findByStatus(OrderStatus status);

  List<BookOrder> findByUserEmailAndBookEntryId(String email, int id);
}
