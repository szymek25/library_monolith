package pl.szymanski.springfrontend.dao;

import org.springframework.data.repository.CrudRepository;
import pl.szymanski.springfrontend.model.BookEntry;

public interface BookEntryDao extends CrudRepository<BookEntry, Integer> {

}
