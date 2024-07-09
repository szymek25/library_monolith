package pl.szymanski.springfrontend.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.szymanski.springfrontend.barcode.BarcodeDecoder;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.dtos.OrderDTO;
import pl.szymanski.springfrontend.exceptions.BarcodeDecodingException;
import pl.szymanski.springfrontend.exceptions.OrderException;
import pl.szymanski.springfrontend.exceptions.RentException;
import pl.szymanski.springfrontend.facade.OrderFacade;
import pl.szymanski.springfrontend.facade.UserFacade;

@Controller
@RequestMapping("/orders")
public class OrderController extends AbstractPageController {

  private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

  @Autowired
  private OrderFacade orderFacade;

  @Autowired
  private UserFacade userFacade;

  @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
  public String createOrder(@RequestParam("bookEntryId") final String bookEntryId, final
  RedirectAttributes redirectAttributes) {

    try {
      final int book = Integer.parseInt(bookEntryId);
      orderFacade.createOrder(book);

    } catch (Exception e) {
      addFlashConfMessage("orders.create.error", redirectAttributes);
      return REDIRECT_PREFIX + "/books/list";
    }

    addFlashConfMessage("orders.create.conf", redirectAttributes);
    return REDIRECT_PREFIX + "/orders/list/user";
  }

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String allOrders(@RequestParam("page") final Optional<Integer> page,
      final Model model) {
    final Integer currentPage = page.orElse(0);
    final PageRequest pageable = new PageRequest(currentPage,
        ApplicationConstants.DEFAULT_PAGE_SIZE);
    final Page<OrderDTO> paginatedOrders = orderFacade.getAllPaginated(pageable);

    addPaginationResult(currentPage, "orders", paginatedOrders, model);
    return "orders";
  }

  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  @RequestMapping(value = "/ordersForUser", method = RequestMethod.GET)
  public String findOrdersByUserBarCode(@RequestParam("barcode") final String barcode,
      @RequestParam("page") final Optional<Integer> page,
      final Model model, final RedirectAttributes redirect) {
    Integer decodedBarcode;
    try {
      decodedBarcode = BarcodeDecoder.decodeUserBarCode(barcode);
    } catch (BarcodeDecodingException e) {
      LOG.warn(e.getMessage());
      addFlashErrorMessage("search.with.barcode.invalidBarcode", redirect);
      return REDIRECT_PREFIX + "/orders/list";
    }
    final Integer currentPage = page.orElse(0);
    final PageRequest pageable = new PageRequest(currentPage,
        ApplicationConstants.DEFAULT_PAGE_SIZE);
    final Page<OrderDTO> paginatedRents = orderFacade.getAllPaginatedForUser(pageable, decodedBarcode);
    if (paginatedRents.isEmpty()) {
      addFlashErrorMessage("orders.list.findByUser.empty", redirect);
      return REDIRECT_PREFIX + "/orders/list";
    }

    final Map<String, String> paramsToPass = new HashMap<>();
    paramsToPass.put("barcode", barcode);
    addPaginationResult(currentPage, "orders", paginatedRents, model, paramsToPass);

    return "orders";
  }

  @RequestMapping(value = "/list/user", method = RequestMethod.GET)
  public String userOrders(@RequestParam("page") final Optional<Integer> page,
      final Model model) {
    final Integer currentPage = page.orElse(0);
    final PageRequest pageable = new PageRequest(currentPage,
        ApplicationConstants.DEFAULT_PAGE_SIZE);
    final Page<OrderDTO> paginatedOrders = orderFacade
        .getAllPaginatedForCurrentUser(pageable);

    addPaginationResult(currentPage, "orders", paginatedOrders, model);
    return "orders";
  }

  @RequestMapping(value = "/cancel/{id}", method = RequestMethod.POST)
  public String cancelOrder(@PathVariable("id") final int id,
      final RedirectAttributes redirectAttributes) {
    try {
      orderFacade.cancelOrder(id);
    } catch (OrderException e) {
      addFlashErrorMessage("orders.status.set.error", redirectAttributes);
      LOG.error(e.getMessage());
    }
    addFlashConfMessage("orders.status.set.success", redirectAttributes);
    final Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext()
        .getAuthentication().getAuthorities();
    if (hasUserRole(authorities)) {

      return REDIRECT_PREFIX + "/orders/list/user";
    } else {
      return REDIRECT_PREFIX + "/orders/list";
    }
  }

  private boolean hasUserRole(final Collection<? extends GrantedAuthority> authorities) {
    return authorities.stream()
        .anyMatch(
            authority -> ApplicationConstants.USER_ROLE_NAME.equals(authority.getAuthority()));
  }

  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  @RequestMapping(value = "/confirm/{id}", method = RequestMethod.POST)
  public String confirmOrder(@PathVariable("id") final int id,
      final RedirectAttributes redirectAttributes) {
    try {
      orderFacade.confirmOrder(id);
    } catch (OrderException e) {
      addFlashErrorMessage("orders.status.set.error", redirectAttributes);
      LOG.error(e.getMessage());
    }
    addFlashConfMessage("orders.status.set.success", redirectAttributes);

    return REDIRECT_PREFIX + "/orders/list";
  }

  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  @RequestMapping(value = "/finish/{id}", method = RequestMethod.POST)
  public String finishOrder(@PathVariable("id") final int id,
      final RedirectAttributes redirectAttributes) {
    try {
      orderFacade.finishOrder(id);
    } catch (OrderException e) {
      addFlashErrorMessage("orders.status.set.error", redirectAttributes);
      LOG.error(e.getMessage());
      return REDIRECT_PREFIX + "/orders/list";
    } catch (RentException e) {
      return REDIRECT_PREFIX + "/rents/list?success=false";
    }

    return REDIRECT_PREFIX + "/rents/list?success=true";
  }
}
