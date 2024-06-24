package pl.szymanski.springfrontend.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.szymanski.springfrontend.barcode.BarcodeDecoder;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.dtos.ReservationDTO;
import pl.szymanski.springfrontend.exceptions.BarcodeDecodingException;
import pl.szymanski.springfrontend.exceptions.RentException;
import pl.szymanski.springfrontend.exceptions.ReservationException;
import pl.szymanski.springfrontend.facade.ReservationFacade;

@Controller
@RequestMapping(value = "/reservations")
public class ReservationController extends AbstractPageController {

  private static final Logger LOG = LoggerFactory.getLogger(ReservationController.class);

  @Autowired
  private ReservationFacade reservationFacade;

  @RequestMapping(value = "/createReservation", method = RequestMethod.POST)
  public String createReservation(@RequestParam("bookEntryId") final String bookEntryId, final
  RedirectAttributes redirectAttributes) {

    try {
      final int book = Integer.parseInt(bookEntryId);
      reservationFacade.createReservation(book);

    } catch (Exception e) {
      addFlashConfMessage("reservation.create.error", redirectAttributes);
      return REDIRECT_PREFIX + "/books/list";
    }

    addFlashConfMessage("reservation.create.conf", redirectAttributes);
    return REDIRECT_PREFIX + "/reservations/list/user";
  }

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String allReservation(@RequestParam("page") final Optional<Integer> page,
      final Model model) {
    final Integer currentPage = page.orElse(0);
    final PageRequest pageable = new PageRequest(currentPage,
        ApplicationConstants.DEFAULT_PAGE_SIZE);
    final Page<ReservationDTO> paginatedReservation = reservationFacade.getAllPaginated(pageable);

    addPaginationResult(currentPage, "reservations", paginatedReservation, model);
    return "reservations";
  }

  @RequestMapping(value = "/list/user", method = RequestMethod.GET)
  public String userReservations(@RequestParam("page") final Optional<Integer> page,
      final Model model) {

    final Integer currentPage = page.orElse(0);
    final PageRequest pageable = new PageRequest(currentPage,
        ApplicationConstants.DEFAULT_PAGE_SIZE);
    final Page<ReservationDTO> paginatedReservations = reservationFacade
        .getReservationsForCurrentUser(pageable);

    addPaginationResult(currentPage, "reservations", paginatedReservations, model);
    return "reservations";
  }

  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  @RequestMapping(value = "/reservationsForUser", method = RequestMethod.GET)
  public String findReservationsByUserBarCode(@RequestParam("barcode") final String barcode,
      @RequestParam("page") final Optional<Integer> page,
      final Model model, final RedirectAttributes redirect) {
    Integer decodedBarcode;
    try {
      decodedBarcode = BarcodeDecoder.decodeUserBarCode(barcode);
    } catch (BarcodeDecodingException e) {
      LOG.warn(e.getMessage());
      addFlashErrorMessage("search.with.barcode.invalidBarcode", redirect);
      return REDIRECT_PREFIX + "/reservations/list";
    }
    final Integer currentPage = page.orElse(0);
    final PageRequest pageable = new PageRequest(currentPage,
        ApplicationConstants.DEFAULT_PAGE_SIZE);
    final Page<ReservationDTO> paginatedReservations = reservationFacade
        .getAllReservationForUser(pageable, decodedBarcode);
    if (paginatedReservations.isEmpty()) {
      addFlashErrorMessage("reservations.list.findByUser.empty", redirect);
      return REDIRECT_PREFIX + "/reservations/list";
    }

    final Map<String, String> paramsToPass = new HashMap<>();
    paramsToPass.put("barcode", barcode);
    addPaginationResult(currentPage, "reservations", paginatedReservations, model, paramsToPass);

    return "reservations";
  }

  @RequestMapping(value = "/cancel/{id}", method = RequestMethod.POST)
  public String cancelReservation(@PathVariable("id") final int id,
      final RedirectAttributes redirectAttributes) {
    try {
      reservationFacade.cancelReservation(id);
    } catch (ReservationException e) {
      addFlashErrorMessage("reservation.status.set.error", redirectAttributes);
      LOG.error(e.getMessage());
    }
    addFlashConfMessage("reservation.status.set.success", redirectAttributes);

    return REDIRECT_PREFIX + "/reservations/list/user";

  }

  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  @RequestMapping(value = "/finish/{id}", method = RequestMethod.POST)
  public String finishReservation(@PathVariable("id") final int id,
      final RedirectAttributes redirectAttributes) {
    try {
      reservationFacade.finishReservation(id);
    } catch (ReservationException e) {
      addFlashErrorMessage("reservation.status.set.error", redirectAttributes);
      LOG.error(e.getMessage());
      return REDIRECT_PREFIX + "/reservation/list";
    } catch (RentException e) {
      LOG.error(e.getMessage());
      return REDIRECT_PREFIX + "/rents/list?success=false";
    }

    return REDIRECT_PREFIX + "/rents/list?success=true";
  }
}
