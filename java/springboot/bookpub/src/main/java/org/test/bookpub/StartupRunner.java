package org.test.bookpub;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.test.bookpub.repository.BookRepository;

import javax.sql.DataSource;

/**
 * Created by yangyunsheng on 2017/7/12.
 */

@Order(value = 1)
public class StartupRunner implements CommandLineRunner{

    protected  final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DataSource ds;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... strings) throws Exception {
//        logger.info("dataSource:" + ds.toString());
        logger.info("number of books:" + bookRepository.count());
    }

    @Scheduled(initialDelay = 1000, fixedRate = 10000)
    public void run(){
        logger.info("number of books:" + bookRepository.count());
    }
}
