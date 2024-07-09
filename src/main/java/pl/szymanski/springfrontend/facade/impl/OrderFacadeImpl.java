package pl.szymanski.springfrontend.facade.impl;

import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.szymanski.springfrontend.dtos.CreateOrderDTO;
import pl.szymanski.springfrontend.dtos.OrderDTO;
import pl.szymanski.springfrontend.enums.OrderStatus;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.exceptions.OrderException;
import pl.szymanski.springfrontend.exceptions.RentException;
import pl.szymanski.springfrontend.facade.OrderFacade;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.model.BookOrder;
import pl.szymanski.springfrontend.model.User;
import pl.szymanski.springfrontend.service.BookEntryService;
import pl.szymanski.springfrontend.service.DepartmentService;
import pl.szymanski.springfrontend.service.OrderService;
import pl.szymanski.springfrontend.service.RentService;
import pl.szymanski.springfrontend.service.UserService;

@Component
public class OrderFacadeImpl implements OrderFacade {

  @Autowired
  private OrderService orderService;

  @Autowired
  private RentService rentService;

  @Autowired
  private UserService userService;

  @Autowired
  private BookEntryService bookEntryService;

  @Autowired
  private DepartmentService departmentService;

  @Override
  public OrderDTO createOrder(final int bookEntryId) throws BookException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return new OrderDTO(
        orderService.createOrder(new CreateOrderDTO(bookEntryId, authentication.getName())));
  }

  @Override
  public Page<OrderDTO> getAllPaginated(Pageable pageable) {
    Page<BookOrder> orders = orderService.getAllPaginated(pageable);
    return convertToPageWithOrderDTO(orders);
  }

  @Override
  public Page<OrderDTO> getAllPaginatedForUser(Pageable pageable, int userId) {
    Page<BookOrder> orders = orderService.getAllPaginatedForUser(pageable, userId);
    return convertToPageWithOrderDTO(orders);
  }

  @Override
  public Page<OrderDTO> getAllPaginatedForCurrentUser(Pageable pageable) {
    User user = userService.getCurrentUser();
    Page<BookOrder> orders = orderService.getAllPaginatedForUser(pageable, user.getId());
    return convertToPageWithOrderDTO(orders);
  }

  @Override
  public OrderDTO cancelOrder(int orderId) throws OrderException {
    return new OrderDTO(orderService.cancelOrder(orderId));
  }

  @Override
  public OrderDTO confirmOrder(int orderId) throws OrderException {
    return new OrderDTO(orderService.confirmOrder(orderId));
  }

  @Override
  @Transactional
  public OrderDTO finishOrder(int orderId) throws OrderException, RentException {
    final BookOrder bookOrder = orderService.finishOrder(orderId);
    final User user = userService.getUserByEmail(bookOrder.getUserEmail());
    final BookEntry bookEntry = bookEntryService.getBookEntryById(bookOrder.getBookEntryId());
    if (user == null || bookEntry == null) {
      throw new OrderException("Invalid input data, user or bookEntry was null");
    }
    rentService.createNewRent(bookEntry, user, false);

    return new OrderDTO(bookOrder);
  }

  @Override
  public int countNewOrders() {
    final List<BookOrder> orders = orderService.getOrdersByStatus(OrderStatus.NEW);
    return CollectionUtils.isNotEmpty(orders) ? orders.size() : 0;
  }

  private Page<OrderDTO> convertToPageWithOrderDTO(Page<BookOrder> orders) {
    return orders != null ? orders.map(this::createOrderDto) : null;
  }

  protected OrderDTO createOrderDto(final BookOrder order) {
    final OrderDTO orderDTO = new OrderDTO(order);
    final BookEntry bookEntry = bookEntryService.getBookEntryById(order.getBookEntryId());
    if (bookEntry != null) {
      orderDTO.setCurrentEmployeeHasPermissionToBook(
          departmentService.checkIfCurrentUserHasPermissionToBook(bookEntry));
    }

    return orderDTO;
  }
}
