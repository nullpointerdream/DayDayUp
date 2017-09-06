package org.test.bookpub;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.test.bookpub.entity.Author;
import org.test.bookpub.entity.Book;
import org.test.bookpub.entity.Publisher;
import org.test.bookpub.repository.AuthorRepository;
import org.test.bookpub.repository.BookRepository;
import org.test.bookpub.repository.PublisherRepository;

import javax.sql.DataSource;

/**
 * Created by yangyunsheng on 2017/7/12.
 */

@Order(value = 1)
public class StartupRunner implements CommandLineRunner {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DataSource ds;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Override
    public void run(String... strings) throws Exception {
        logger.info("dataSource:" + ds.toString());
        // 增加一条记录
        Author author = new Author("yunsheng", "yang");
        author = authorRepository.save(author);
        Publisher publisher = new Publisher("yunsheng");
        publisher = publisherRepository.save(publisher);
        Book book = new Book("1001", "test1", author, publisher);
        Book book1 = bookRepository.save(book);
        logger.info("number of books:" + bookRepository.count());
    }

    @Scheduled(initialDelay = 1000, fixedRate = 10000)
    public void run() {
        logger.info("number of books:" + bookRepository.count());
    }
}
