package pl.szymanski.springfrontend.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.szymanski.springfrontend.model.Book;
import pl.szymanski.springfrontend.model.Category;

public interface BookDao extends CrudRepository<Book, Integer> {

  boolean existsByIsbn(String isbn);

  Page<Book> findAll(Pageable pageable);

  @Query("select b from Book b Join Category c on b.category = c.id where c.id In (?1)")
  Page<Book> findByCategories(Integer[] categories, Pageable pageable);

  List<Book> findByCategory(Category category);
}
