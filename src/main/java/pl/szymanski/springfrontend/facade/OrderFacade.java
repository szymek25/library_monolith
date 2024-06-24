package pl.szymanski.springfrontend.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.szymanski.springfrontend.dtos.OrderDTO;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.exceptions.OrderException;
import pl.szymanski.springfrontend.exceptions.RentException;

public interface OrderFacade {

  OrderDTO createOrder(int bookEntryId) throws BookException;

  Page<OrderDTO> getAllPaginated(Pageable pageable);

  Page<OrderDTO> getAllPaginatedForUser(Pageable pageable, int userId);

  OrderDTO cancelOrder(int orderId) throws OrderException;

  OrderDTO confirmOrder(int orderId) throws OrderException;

  OrderDTO finishOrder(int orderId) throws OrderException, RentException;

  int countNewOrders();
}
