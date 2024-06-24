package pl.szymanski.springfrontend.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
import pl.szymanski.springfrontend.dtos.BookEntryDTO;
import pl.szymanski.springfrontend.dtos.CreateRentDTO;
import pl.szymanski.springfrontend.dtos.RentDTO;
import pl.szymanski.springfrontend.dtos.UserDTO;
import pl.szymanski.springfrontend.exceptions.BarcodeDecodingException;
import pl.szymanski.springfrontend.exceptions.ProlongationException;
import pl.szymanski.springfrontend.exceptions.RentException;
import pl.szymanski.springfrontend.facade.BookEntryFacade;
import pl.szymanski.springfrontend.facade.RentFacade;
import pl.szymanski.springfrontend.facade.UserFacade;

@Controller
@RequestMapping("/rents")
public class RentController extends AbstractPageController {

  private static final Logger LOG = LoggerFactory.getLogger(RentController.class);

  @Autowired
  private RentFacade rentFacade;

  @Autowired
  private UserFacade userFacade;

  @Autowired
  private BookEntryFacade bookEntryFacade;

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String allRents(@RequestParam("page") final Optional<Integer> page,
      final Model model, @RequestParam(value = "success", required = false) final String success) {
    final Integer currentPage = page.orElse(0);
    final PageRequest pageable = new PageRequest(currentPage,
        ApplicationConstants.DEFAULT_PAGE_SIZE);
    final Page<RentDTO> paginatedRents = rentFacade.getAllPaginated(pageable);

    model.addAttribute("success", success);
    addPaginationResult(currentPage, "rents", paginatedRents, model);
    return "rents";
  }

  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  @RequestMapping(value = "/rentsForUser", method = RequestMethod.GET)
  public String findRentByUserBarCode(@RequestParam("barcode") final String barcode,
      @RequestParam("page") final Optional<Integer> page,
      final Model model, final RedirectAttributes redirect) {
    Integer decodedBarcode;
    try {
      decodedBarcode = BarcodeDecoder.decodeUserBarCode(barcode);
    } catch (BarcodeDecodingException e) {
      LOG.warn(e.getMessage());
      addFlashErrorMessage("search.with.barcode.invalidBarcode", redirect);
      return REDIRECT_PREFIX + "/rents/list";
    }
    final Integer currentPage = page.orElse(0);
    final PageRequest pageable = new PageRequest(currentPage,
        ApplicationConstants.DEFAULT_PAGE_SIZE);
    final Page<RentDTO> paginatedRents = rentFacade
        .getAllPaginatedForUser(pageable, decodedBarcode);
    if (paginatedRents.isEmpty()) {
      addFlashErrorMessage("rents.list.findByUser.empty", redirect);
      return REDIRECT_PREFIX + "/rents/list";
    }

    final Map<String, String> paramsToPass = new HashMap<>();
    paramsToPass.put("barcode", barcode);
    addPaginationResult(currentPage, "rents", paginatedRents, model, paramsToPass);

    return "rents";
  }

  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  @RequestMapping(value = "/rentsForBook", method = RequestMethod.GET)
  public String findRentByBookBarCode(@RequestParam("barcode") final String barcode,
      @RequestParam("page") final Optional<Integer> page,
      final Model model, final RedirectAttributes redirect) {
    Integer decodedBarcode;
    try {
      decodedBarcode = BarcodeDecoder.decodeBookBarCode(barcode);
    } catch (BarcodeDecodingException e) {
      LOG.warn(e.getMessage());
      addFlashErrorMessage("search.with.barcode.invalidBarcode", redirect);
      return REDIRECT_PREFIX + "/rents/list";
    }
    final Integer currentPage = page.orElse(0);
    final PageRequest pageable = new PageRequest(currentPage,
        ApplicationConstants.DEFAULT_PAGE_SIZE);
    final Page<RentDTO> paginatedRents = rentFacade
        .getAllPaginatedForBookEntry(pageable, decodedBarcode);
    if (paginatedRents.isEmpty()) {
      addFlashErrorMessage("rents.list.findByBook.empty", redirect);
      return REDIRECT_PREFIX + "/rents/list";
    }

    final Map<String, String> paramsToPass = new HashMap<>();
    paramsToPass.put("barcode", barcode);
    addPaginationResult(currentPage, "rents", paginatedRents, model, paramsToPass);

    return "rents";
  }

  @RequestMapping(value = "/end", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String endRent(@RequestParam("rentId") final int id,
      @RequestParam(value = "physicalState", required = false) final String physicalState) {
    rentFacade.endRent(id, physicalState);

    return REDIRECT_PREFIX + "/rents/list";
  }

  @RequestMapping(value = "/list/user", method = RequestMethod.GET)
  public String userRent(@RequestParam("page") final Optional<Integer> page,
      final Model model) {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final User authToken = (User) auth.getPrincipal();

    final Integer currentPage = page.orElse(0);
    final PageRequest pageable = new PageRequest(currentPage,
        ApplicationConstants.DEFAULT_PAGE_SIZE);
    final Page<RentDTO> paginatedRents = rentFacade
        .getAllPaginatedForUser(pageable, userFacade.getUserIdByEmail(authToken.getUsername()));

    addPaginationResult(currentPage, "rents", paginatedRents, model);
    return "rents";
  }

  @RequestMapping(value = "/selectUser/{entryId}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String selectUser(@PathVariable("entryId") final int entryId,
      @RequestParam("page") final Optional<Integer> page, final Model model) {
    final BookEntryDTO bookEntry = bookEntryFacade.getBookEntryById(entryId);
    if (bookEntry == null) {
      return REDIRECT_PREFIX + "books/list";
    }
    model.addAttribute("bookEntry", bookEntry);
    final Integer currentPage = page.orElse(0);
    final Pageable pageable = new PageRequest(currentPage, ApplicationConstants.DEFAULT_PAGE_SIZE);
    final Page<UserDTO> paginatedUser = userFacade.getPaginatedLibraryCustomers(pageable);
    addPaginationResult(currentPage, "users", paginatedUser, model);

    return "selectUser";
  }

  @RequestMapping(value = "/createRent", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String createRent(@RequestParam("userId") final String userId,
      @RequestParam("bookEntryId") final String bookEntryId) {

    try {
      final int user = Integer.parseInt(userId);
      final int book = Integer.parseInt(bookEntryId);
      rentFacade.createNewRent(new CreateRentDTO(book, user));

    } catch (Exception e) {
      return REDIRECT_PREFIX + "/rents/list?success=false";
    }

    return REDIRECT_PREFIX + "/rents/list?success=true";
  }

  @RequestMapping(value = "/createRentByBarcode", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String createRentByUserBarcode(@RequestParam("barcode") final String barcode,
      @RequestParam("bookEntryId") final String bookEntryId, final RedirectAttributes redirect) {

    try {
      final int user = BarcodeDecoder.decodeUserBarCode(barcode);
      final int book = Integer.parseInt(bookEntryId);
      rentFacade.createNewRent(new CreateRentDTO(book, user));

    } catch (BarcodeDecodingException e) {
      LOG.warn(e.getMessage());
      addFlashErrorMessage("search.with.barcode.invalidBarcode", redirect);
      return REDIRECT_PREFIX + "/rents/selectUser/" + bookEntryId;
    } catch (Exception e) {
      LOG.error(e.getMessage());
      return REDIRECT_PREFIX + "/rents/list?success=false";
    }
    return REDIRECT_PREFIX + "/rents/list?success=true";
  }

  @RequestMapping(value = "/prolongation", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_USER')")
  public String performProlongation(@RequestParam("rentId") final int id,
      final RedirectAttributes redirect) {
    try {
      rentFacade.performProlongation(id);
    } catch (RentException e) {
      LOG.error(e.getMessage());
      addFlashErrorMessage("rents.list.prolongation.error", redirect);
    } catch (ProlongationException e) {
      LOG.error(e.getMessage());
      addFlashErrorMessage("rents.list.prolongation.arleadyPerformed", redirect);
    }

    return REDIRECT_PREFIX + "/rents/list/user";
  }
}
