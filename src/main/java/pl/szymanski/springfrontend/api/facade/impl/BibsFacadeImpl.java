package pl.szymanski.springfrontend.api.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.api.dto.BookApiDTO;
import pl.szymanski.springfrontend.api.facade.BibsFacade;
import pl.szymanski.springfrontend.api.service.BibsIntegrationService;

@Component
public class BibsFacadeImpl implements BibsFacade {

  @Autowired
  private BibsIntegrationService bibsIntegrationService;

  @Override
  public BookApiDTO findBookByIsbn(final String isbn) {
    return bibsIntegrationService.findBookByIsbn(isbn);
  }
}
