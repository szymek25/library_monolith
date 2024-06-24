package pl.szymanski.springfrontend.service.impl;

import static pl.szymanski.springfrontend.service.impl.RentServiceImpl.EUROPE_WARSAW;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.dao.OrderDao;
import pl.szymanski.springfrontend.dtos.CreateOrderDTO;
import pl.szymanski.springfrontend.enums.OrderStatus;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.exceptions.OrderException;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.model.BookOrder;
import pl.szymanski.springfrontend.model.Department;
import pl.szymanski.springfrontend.model.User;
import pl.szymanski.springfrontend.service.BookEntryService;
import pl.szymanski.springfrontend.service.EmailService;
import pl.szymanski.springfrontend.service.OrderService;
import pl.szymanski.springfrontend.service.ReservationService;
import pl.szymanski.springfrontend.service.UserService;

@Service
public class OrderServiceImpl implements OrderService {

  private static Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);
  private static final Collection activeOrderStatuses = Arrays
      .asList(OrderStatus.NEW, OrderStatus.CONFIRMED);


  @Autowired
  private OrderDao orderDao;

  @Autowired
  private UserService userService;

  @Autowired
  private BookEntryService bookEntryService;

  @Autowired
  private EmailService emailService;

  @Autowired
  private ReservationService reservationService;

  @Override
  public BookOrder createOrder(final CreateOrderDTO createOrderDTO) throws BookException {
    User user = userService.getUserByEmail(createOrderDTO.getUserEmail());
    if (user == null) {
      throw new IllegalStateException(
          "User with id: " + createOrderDTO.getUserEmail() + " doesn`t exists");
    }

    BookEntry bookEntry = bookEntryService.getBookEntryById(createOrderDTO.getBookEntryId());
    if (bookEntry == null) {
      throw new BookException("Not Found book entry with id: " + createOrderDTO.getBookEntryId());
    }

    return createOrder(user, bookEntry);
  }

  @Override
  public Page<BookOrder> getAllPaginated(Pageable pageable) {
    return orderDao.findAll(pageable);
  }

  @Override
  public Page<BookOrder> getAllPaginatedForUser(Pageable pageable, int userId) {
    User user = userService.getUserById(userId);
    return orderDao.findByUserEmail(pageable, user.getEmail());
  }

  @Override
  public BookOrder cancelOrder(int orderId) throws OrderException {
    final BookOrder bookOrder = orderDao.findById(orderId)
        .orElseThrow(() -> new OrderException("Not found order with id: " + orderId));
    bookOrder.setStatus(OrderStatus.CANCELED);
    orderDao.save(bookOrder);
    final BookEntry bookEntry = bookEntryService.getBookEntryById(bookOrder.getBookEntryId());
    reservationService.handleReservationsAfterCancelOrder(bookEntry);

    return bookOrder;
  }

  @Override
  public BookOrder confirmOrder(int orderId) throws OrderException {
    final BookOrder bookOrder = orderDao.findById(orderId)
        .orElseThrow(() -> new OrderException("Not found order with id: " + orderId));
    bookOrder.setStatus(OrderStatus.CONFIRMED);
    bookOrder.setTimeFromOrderIsReadyForCollect(new Timestamp(System.currentTimeMillis()));
    orderDao.save(bookOrder);

    new Thread(() -> emailService.notifyAboutOrderIsReadyForCollect(bookOrder)).start();

    return bookOrder;
  }

  @Override
  public List<BookOrder> getBookOrdersForBookEntry(BookEntry bookEntry) {
    if (bookEntry != null) {
      return orderDao.findByBookEntryId(bookEntry.getBookEntry_id());
    }
    return null;
  }

  @Override
  public BookOrder finishOrder(int orderId) throws OrderException {
    final BookOrder bookOrder = orderDao.findById(orderId)
        .orElseThrow(() -> new OrderException("Not found order with id: " + orderId));
    bookOrder.setStatus(OrderStatus.FINISHED);
    orderDao.save(bookOrder);

    return bookOrder;
  }

  @Override
  public List<BookOrder> getOrdersByStatus(OrderStatus status) {
    return orderDao.findByStatus(status);
  }

  @Override
  public void checkAllUncollectedOrders() {
    final List<BookOrder> ordersReadyForCollect = orderDao
        .findByStatus(OrderStatus.CONFIRMED);
    LocalDateTime now = LocalDateTime.now();
    final List<BookOrder> unCollectedOrders = ordersReadyForCollect.stream()
        .filter(bookOrder -> {
          final LocalDateTime timeFromReadyForCollect = bookOrder
              .getTimeFromOrderIsReadyForCollect().toLocalDateTime();

          return timeFromReadyForCollect
              .plusDays(ApplicationConstants.DAYS_FOR_COLLECT_BOOK_AFTER_ORDER).isBefore(now);
        })
        .collect(Collectors.toList());

    unCollectedOrders.forEach(order -> {
      try {
        cancelOrder(order.getId());
        emailService.cancelOrderNotification(order);
      } catch (OrderException e) {
        LOG.error("Couldn`t cancel order with id:{}", order.getId());
      }
    });
  }

  @Override
  public boolean checkIfCurrentUserHasActiveOrder(BookEntry bookEntry) {
    final User currentUser = userService.getCurrentUser();
    final List<BookOrder> orders = orderDao
        .findByUserEmailAndBookEntryId(currentUser.getEmail(), bookEntry.getBookEntry_id());

    return hasActiveOrder(orders);
  }

  private BookOrder createOrder(final User user, final BookEntry bookEntry) {
    final BookOrder order = new BookOrder();
    ZonedDateTime startTime = LocalDateTime.now().atZone(ZoneId.of(EUROPE_WARSAW));
    order.setCreated(new Date(startTime.toInstant().toEpochMilli()));
    order.setBookEntryId(bookEntry.getBookEntry_id());
    order.setUserEmail(user.getEmail());
    order.setBookInventoryNumber(bookEntry.getInventoryNumber());
    order.setBookTitle(bookEntry.getBook().getTitle());
    order.setStatus(OrderStatus.NEW);
    final Department department = bookEntry.getDepartment();
    if (department != null) {
      order.setDepartmentName(department.getName());
    }
    orderDao.save(order);
    return order;
  }


  public boolean hasActiveOrder(List<BookOrder> orders) {
    return orders.stream()
        .anyMatch(order -> activeOrderStatuses.contains(order.getStatus()));
  }
}
