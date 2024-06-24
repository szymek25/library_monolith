package pl.szymanski.springfrontend.api.facade;

import pl.szymanski.springfrontend.api.dto.BookApiDTO;

public interface BibsFacade {

  BookApiDTO findBookByIsbn(String isbn);
}
