package pl.szymanski.springfrontend.api.dto;

import java.util.List;

public class BookApiDTO {

  List<BookBibsDTO> bibs;

  public List<BookBibsDTO> getBibs() {
    return bibs;
  }

  public void setBibs(List<BookBibsDTO> bibs) {
    this.bibs = bibs;
  }
}
