package pl.szymanski.springfrontend.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.dtos.BookEntryDTO;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.facade.BookEntryFacade;
import pl.szymanski.springfrontend.forms.BookEntryForm;
import pl.szymanski.springfrontend.forms.EditBookEntryForm;
import pl.szymanski.springfrontend.model.BookEntry;
import pl.szymanski.springfrontend.service.BookEntryService;
import pl.szymanski.springfrontend.service.DepartmentService;
import pl.szymanski.springfrontend.service.RentService;

@Component
public class BookEntryFacadeImpl implements BookEntryFacade {

  @Autowired
  private BookEntryService bookEntryService;

  @Autowired
  private RentService rentService;

  @Autowired
  private DepartmentService departmentService;

  @Override
  public void addBookEntry(BookEntryForm form) throws BookException {
    final BookEntryDTO bookEntryDTO = new BookEntryDTO();
    bookEntryDTO.setBookId(Integer.parseInt(form.getBookId()));
    bookEntryDTO.setInventoryNumber(form.getInventoryNumber());
    bookEntryDTO.setPhysicalState(form.getPhysicalState());
    bookEntryDTO.setSignature(form.getSignature());

    bookEntryService.addBookEntry(bookEntryDTO);
  }

  @Override
  public BookEntryDTO editBookEntry(EditBookEntryForm form) {
    final BookEntryDTO bookEntryDTO = new BookEntryDTO();
    bookEntryDTO.setId(Integer.parseInt(form.getId()));
    bookEntryDTO.setSignature(form.getSignature());
    bookEntryDTO.setInventoryNumber(form.getInventoryNumber());
    bookEntryDTO.setPhysicalState(form.getPhysicalState());

    final BookEntry bookEntry = bookEntryService.editBookEntry(bookEntryDTO);
    return bookEntry != null ? new BookEntryDTO(bookEntry) : null;
  }

  @Override
  public void removeBookEntry(int id) {
    bookEntryService.removeBookEntry(id);
  }

  @Override
  public BookEntryDTO getBookEntryById(int id) {
    BookEntry bookEntry = bookEntryService.getBookEntryById(id);
    if (bookEntry != null) {
      final BookEntryDTO bookEntryDTO = new BookEntryDTO(bookEntry);
      bookEntryDTO.setCurrentEmployeeHasPermissionToBook(
          departmentService.checkIfCurrentUserHasPermissionToBook(bookEntry));
      bookEntryDTO.setBookRented(rentService.isBookRented(bookEntry));
      return bookEntryDTO;
    }
    return null;
  }

  @Override
  public boolean isBookRented(BookEntryDTO bookEntryDTO) {
    final BookEntry bookEntry = bookEntryService.getBookEntryById(bookEntryDTO.getId());
    return rentService.isBookRented(bookEntry);
  }
}
