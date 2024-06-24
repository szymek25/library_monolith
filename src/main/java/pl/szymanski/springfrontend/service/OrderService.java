package pl.szymanski.springfrontend.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.szymanski.springfrontend.dtos.CreateOrderDTO;
import pl.szymanski.springfrontend.enums.OrderStatus;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.exceptions.OrderException;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.model.BookOrder;

public interface OrderService {

  BookOrder createOrder(CreateOrderDTO createOrderDTO) throws BookException;

  Page<BookOrder> getAllPaginated(Pageable pageable);

  Page<BookOrder> getAllPaginatedForUser(Pageable pageable, int userId);

  BookOrder cancelOrder(int orderId) throws OrderException;

  BookOrder confirmOrder(int orderId) throws OrderException;

  List<BookOrder> getBookOrdersForBookEntry(BookEntry bookEntry);

  BookOrder finishOrder(int orderId) throws OrderException;

  List<BookOrder> getOrdersByStatus(OrderStatus status);

  void checkAllUncollectedOrders();

  boolean checkIfCurrentUserHasActiveOrder(BookEntry bookEntry);

  boolean hasActiveOrder(List<BookOrder> orders);
}
