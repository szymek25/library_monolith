package pl.szymanski.springfrontend.api.service;

import pl.szymanski.springfrontend.api.dto.BookApiDTO;

public interface BibsIntegrationService {

  BookApiDTO findBookByIsbn(String isbn);
}
