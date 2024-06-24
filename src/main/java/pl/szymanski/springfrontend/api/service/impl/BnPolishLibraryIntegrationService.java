package pl.szymanski.springfrontend.api.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.szymanski.springfrontend.api.dto.BookApiDTO;
import pl.szymanski.springfrontend.api.service.BibsIntegrationService;

@Service
public class BnPolishLibraryIntegrationService implements BibsIntegrationService {

  private static final String resourceUrl = "https://data.bn.org.pl/api/bibs.json?isbnIssn=";

  @Override
  public BookApiDTO findBookByIsbn(final String isbn) {
    final RestTemplate restTemplate = new RestTemplate();
    final ResponseEntity<BookApiDTO> result = restTemplate
        .getForEntity(resourceUrl + isbn, BookApiDTO.class);

    return result.getBody();
  }
}
