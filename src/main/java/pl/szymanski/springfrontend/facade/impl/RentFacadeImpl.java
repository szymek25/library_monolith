package pl.szymanski.springfrontend.facade.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.dtos.CreateRentDTO;
import pl.szymanski.springfrontend.dtos.RentDTO;
import pl.szymanski.springfrontend.exceptions.ProlongationException;
import pl.szymanski.springfrontend.exceptions.RentException;
import pl.szymanski.springfrontend.facade.RentFacade;
import pl.szymanski.springfrontend.model.Book;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.model.Category;
import pl.szymanski.springfrontend.model.Rent;
import pl.szymanski.springfrontend.model.User;
import pl.szymanski.springfrontend.service.BookEntryService;
import pl.szymanski.springfrontend.service.DepartmentService;
import pl.szymanski.springfrontend.service.RentService;
import pl.szymanski.springfrontend.service.UserService;

@Component
public class RentFacadeImpl implements RentFacade {

  @Autowired
  private RentService rentService;

  @Autowired
  private UserService userService;

  @Autowired
  private BookEntryService bookEntryService;

  @Autowired
  private DepartmentService departmentService;

  @Override
  public RentDTO createNewRent(CreateRentDTO createRentDTO) throws RentException {
    final User user = userService.getUserById(createRentDTO.getUserId());
    final BookEntry bookEntry = bookEntryService.getBookEntryById(createRentDTO.getBookEntryId());

    if (user == null || bookEntry == null) {
      throw new RentException("Invalid input data, user or bookEntry was null");
    }

    return new RentDTO(rentService.createNewRent(bookEntry, user, false));
  }

  @Override
  public Page<RentDTO> getAllPaginated(Pageable pageable) {
    Page<Rent> rents = rentService.getAllPaginated(pageable);
    return convertToPageWithRentDTO(rents);
  }

  private Page<RentDTO> convertToPageWithRentDTO(Page<Rent> rents) {
    return rents != null ? rents.map(this::createRentDTO) : null;
  }

  protected RentDTO createRentDTO(final Rent rent) {
    final RentDTO rentDTO = new RentDTO(rent);
    final BookEntry bookEntry = bookEntryService.getBookEntryById(rent.getBookEntryId());
    if (bookEntry != null) {
      rentDTO.setCurrentEmployeeHasPermissionToBook(
          departmentService.checkIfCurrentUserHasPermissionToBook(bookEntry));
    }
    setProlongationPossible(rent, rentDTO, bookEntry);

    return rentDTO;
  }

  protected void setProlongationPossible(final Rent rent, final RentDTO rentDTO,
      final BookEntry bookEntry) {
    final Book book = bookEntry.getBook();
    if (book != null && book.getCategory() != null) {
      final Category category = book.getCategory();
      final boolean continuationPossible = BooleanUtils
          .toBoolean(category.isContinuationPossible());
      final boolean prolongationPerformed = BooleanUtils.toBoolean(rent.getProlongationPerformed());
      rentDTO.setProlongationPossible(rent.getReturnDate() == null && continuationPossible && !prolongationPerformed);
    }
  }

  @Override
  public Page<RentDTO> getAllPaginatedForUser(Pageable pageable, int userId) {
    Page<Rent> rents = rentService.getAllPaginatedForUser(pageable, userId);
    return convertToPageWithRentDTO(rents);
  }

  @Override
  public Page<RentDTO> getAllPaginatedForBookEntry(final Pageable pageable, final int bookEntryId) {
    final Page<Rent> rents = rentService.getAllPaginatedForBookEntry(pageable, bookEntryId);
    return convertToPageWithRentDTO(rents);
  }

  @Override
  public String endRent(int rentId, String physicalState) {
    return rentService.endRent(rentId, physicalState);
  }

  @Override
  public RentDTO performProlongation(int rentId) throws RentException, ProlongationException {
    return new RentDTO(rentService.performProlongation(rentId));
  }
}
