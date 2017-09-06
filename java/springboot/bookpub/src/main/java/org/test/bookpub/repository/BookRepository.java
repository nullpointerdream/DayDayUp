package org.test.bookpub.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.test.bookpub.entity.Book;

/**
 * Created by yangyunsheng on 2017/7/13.
 */
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    Book findByIsbn(String isbn);
}
