package pl.szymanski.springfrontend.controllers;

import java.io.ByteArrayInputStream;
import java.util.Locale;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.szymanski.springfrontend.api.dto.BookApiDTO;
import pl.szymanski.springfrontend.api.facade.BibsFacade;
import pl.szymanski.springfrontend.barcode.BarcodeDecoder;
import pl.szymanski.springfrontend.constants.ApplicationConstants;
import pl.szymanski.springfrontend.constants.ExceptionCodes;
import pl.szymanski.springfrontend.dtos.BookDTO;
import pl.szymanski.springfrontend.dtos.BookEntryDTO;
import pl.szymanski.springfrontend.dtos.CategoryDTO;
import pl.szymanski.springfrontend.dtos.DepartmentDTO;
import pl.szymanski.springfrontend.exceptions.BarcodeDecodingException;
import pl.szymanski.springfrontend.exceptions.BookException;
import pl.szymanski.springfrontend.facade.BookEntryFacade;
import pl.szymanski.springfrontend.facade.BookFacade;
import pl.szymanski.springfrontend.facade.CategoryFacade;
import pl.szymanski.springfrontend.facade.DepartmentFacade;
import pl.szymanski.springfrontend.forms.BookEntryForm;
import pl.szymanski.springfrontend.forms.BookForm;
import pl.szymanski.springfrontend.forms.EditBookEntryForm;
import pl.szymanski.springfrontend.pdf.GeneratePDFUtil;

@Controller
@RequestMapping("/books")
public class BookController extends AbstractPageController {

  private static final Logger LOG = LoggerFactory.getLogger(BookController.class);

  @Autowired
  private BookFacade bookFacade;

  @Autowired
  private CategoryFacade categoryFacade;

  @Autowired
  private BookEntryFacade bookEntryFacade;

  @Autowired
  private BibsFacade bibsFacade;

  @Autowired
  private GeneratePDFUtil generatePDFUtil;

  @Autowired
  private DepartmentFacade departmentFacade;

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String allBooks(@RequestParam("page") final Optional<Integer> page,
      final Model model) {
    final Integer currentPage = page.orElse(0);
    final PageRequest pageable = new PageRequest(currentPage,
        ApplicationConstants.DEFAULT_PAGE_SIZE);
    final Page<BookDTO> paginatedBooks = bookFacade.getPaginatedBooks(pageable, null);

    addPaginationResult(currentPage, "books", paginatedBooks, model);
    return "books";
  }

  @RequestMapping(value = "new", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String addNewBookView(final Model model) {
    model.addAttribute("addBookForm", new BookForm());
    model.addAttribute("categories", categoryFacade.getAllCategories());

    return "addNewBook";
  }

  @RequestMapping(value = "/new", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String addNewBook(@ModelAttribute("addBookForm") final @Valid BookForm form,
      BindingResult result, final Model model) {

    if (result.hasErrors()) {
      addSelectedCategoryToModel(form.getCategoryId(), model);

      return "addNewBook";
    }

    try {
      bookFacade.addNewBook(form);
    } catch (BookException e) {
      if (ExceptionCodes.BOOK_ARLEADY_EXSIST.equals(e.getMessage())) {
        addSelectedCategoryToModel(form.getCategoryId(), model);
        addGlobalErrorMessage("books.add.bookExists", model);
      } else {
        addGlobalErrorMessage("books.add.error", model);
      }
      return "addNewBook";
    }

    return REDIRECT_PREFIX + "/books/list";
  }

  @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String editBookView(@PathVariable("id") int id, Model model) {
    BookDTO bookDTO = bookFacade.getBookById(id);
    BookForm bookForm = prepareBookForm(bookDTO);
    model.addAttribute("editBookForm", bookForm);
    addSelectedCategoryToModel(bookForm.getCategoryId(), model);

    return "bookEdit";
  }

  @RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String editBook(@PathVariable("id") final int id,
      @ModelAttribute("editBookForm") @Valid final BookForm form, final BindingResult result,
      final Model model) {
    if (result.hasErrors()) {
      addSelectedCategoryToModel(form.getCategoryId(), model);
      return "bookEdit";
    }

    try {
      bookFacade.updateBook(id, form);

    } catch (BookException e) {
      switch (e.getMessage()) {
        case ExceptionCodes.BOOK_ARLEADY_EXSIST:
          addSelectedCategoryToModel(form.getCategoryId(), model);
          addGlobalErrorMessage("books.edit.bookExists", model);
          return "bookEdit";

        case ExceptionCodes.BOOK_DOES_NOT_EXSIST:
          addSelectedCategoryToModel(form.getCategoryId(), model);
          addGlobalErrorMessage("book.edit.bookDoesNotExists", model);
          return "bookEdit";

      }
    }

    return REDIRECT_PREFIX + "/books/list";
  }

  @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  public String deleteBook(@PathVariable("id") final int id) {
    bookFacade.deleteBook(id);

    return REDIRECT_PREFIX + "/books/list";
  }

  @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
  public String bookDetail(@PathVariable("id") final int id, final Model model,
      @RequestParam(value = "success", required = false) final String success) {
    final BookDTO book = bookFacade.getBookById(id);
    if (book == null) {
      return REDIRECT_PREFIX + "/books/list";
    }

    model.addAttribute("success", success);
    model.addAttribute("book", book);
    model.addAttribute("bookEntryForm", new BookEntryForm());
    model.addAttribute("editBookEntryForm", new EditBookEntryForm());

    return "bookDetail";
  }

  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  @RequestMapping(value = "createEntry/{id}", method = RequestMethod.POST)
  public String createBookEntry(@PathVariable("id") final int id,
      @ModelAttribute("bookEntryForm") final @Valid BookEntryForm form,
      final BindingResult result, final Model model) {
    if (result.hasErrors()) {
      return "bookDetail";
    }

    try {
      bookEntryFacade.addBookEntry(form);
    } catch (BookException e) {
      addGlobalErrorMessage("books.detail.createEntry.error", model);
      return "bookDetail";
    }

    return REDIRECT_PREFIX + "/books/detail/" + id + "?success=true";
  }

  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  @RequestMapping(value = "/detail/{id}/editEntry", method = RequestMethod.POST)
  public String editEntry(@PathVariable("id") final int id,
      @ModelAttribute("editBookEntryForm") final @Valid EditBookEntryForm form,
      BindingResult bindingResult, final Model model) {

    if (bindingResult.hasErrors()) {
      final BookDTO book = bookFacade.getBookById(id);
      model.addAttribute("book", book);
      model.addAttribute("bookEntryForm", new BookEntryForm());
      model.addAttribute("editEntryFormErrors", true);
      return "bookDetail";
    }

    final BookEntryDTO bookEntryDTO = bookEntryFacade.editBookEntry(form);
    if (bookEntryDTO == null) {
      return REDIRECT_PREFIX + "/books/detail/" + id + "?success=false";
    }

    return REDIRECT_PREFIX + "/books/detail/" + id + "?success=true";
  }

  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  @RequestMapping(value = "/detail/{id}/removeEntry", method = RequestMethod.POST)
  public String deleteEntry(@PathVariable("id") final int id,
      @RequestParam(name = "removeEntryId", required = false) final String entryId) {

    bookEntryFacade.removeBookEntry(Integer.parseInt(entryId));
    return REDIRECT_PREFIX + "/books/detail/" + id;
  }

  @RequestMapping(value = "/findBook", method = RequestMethod.POST)
  public String findBookByIsbn(@RequestParam("isbn") final String isbn, final Model model) {
    try {
      final BookApiDTO bookApiDTO = bibsFacade.findBookByIsbn(isbn);
      model.addAttribute("books", bookApiDTO.getBibs());
    } catch (RestClientException e) {
      model.addAttribute("connectionError", true);
    }

    return "searchBookWidget";
  }

  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  @RequestMapping(value = "/printBookLabel/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<InputStreamResource> printBookLabel(final Locale locale,
      @PathVariable("id") final int id) {
    final BookEntryDTO bookEntryDTO = bookEntryFacade.getBookEntryById(id);
    ByteArrayInputStream bis = generatePDFUtil.generateBookLabel(bookEntryDTO, locale);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Disposition", "inline; filename=citiesreport.pdf");

    return ResponseEntity
        .ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_PDF)
        .body(new InputStreamResource(bis));
  }

  @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
  @RequestMapping(value = "/findBookEntry", method = RequestMethod.POST)
  public String findBookEntry(@RequestParam("barcode") final String barcode, final Model model,
      final RedirectAttributes redirect) {
    Integer decodedBarcode;
    try {
      decodedBarcode = BarcodeDecoder.decodeBookBarCode(barcode);
    } catch (BarcodeDecodingException e) {
      LOG.warn(e.getMessage());
      addFlashErrorMessage("search.with.barcode.invalidBarcode", redirect);
      return REDIRECT_PREFIX + "/books/list";
    }
    final BookEntryDTO entry = bookEntryFacade.getBookEntryById(decodedBarcode);
    if (entry == null) {
      addFlashErrorMessage("books.list.findBookEntry.notFound", redirect);
      return REDIRECT_PREFIX + "/books/list";
    }
    model.addAttribute("entry", entry);

    return "searchBookResult";
  }

  @RequestMapping(value = "/departmentInfo", method = RequestMethod.POST)
  public String getDepartmentInfo(@RequestParam("departmentId") final int departmentId,
      final Model model) {
    final DepartmentDTO department = departmentFacade.getDepartmentById(departmentId);
    model.addAttribute("department", department);

    return "departmentInfoWidget";
  }

  private BookForm prepareBookForm(final BookDTO bookDTO) {
    BookForm bookForm = new BookForm();
    bookForm.setTitle(bookDTO.getTitle());
    bookForm.setIsbn(bookDTO.getIsbn());
    bookForm.setAuthor(bookDTO.getAuthor());
    bookForm.setPublicationYear(bookDTO.getPublicationYear());
    bookForm.setPublisher(bookDTO.getPublisher());
    final CategoryDTO categoryDTO = bookDTO.getCategory();
    if (categoryDTO != null) {
      bookForm.setCategoryId(String.valueOf(categoryDTO.getId()));
    }
    return bookForm;
  }

  private void addSelectedCategoryToModel(final String categoryId, final Model model) {
    model.addAttribute("categories", categoryFacade.getAllCategories());
    model.addAttribute("selectedCategory", categoryId);
  }

}
