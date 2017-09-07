package org.test.bookpub;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.test.bookpubstarter.dbcount.DbCountRunner;
import org.test.bookpubstarter.dbcount.EnableDbCounting;

import java.util.Collection;

@SpringBootApplication
@EnableScheduling
@EnableDbCounting
public class BookPubApplication {

//    @Bean
//    public StartupRunner2 schedulerRunner2() {
//        return new StartupRunner2();
//    }

    @Bean
    public StartupRunner schedulerRunner() {
        return new StartupRunner();
    }

//    @Bean
//    public DbCountRunner dbCountRunner(Collection<CrudRepository> repositories) {
//        return new DbCountRunner(repositories) {
//            public void run(String... args) throws Exception {
//                logger.info("Manually Declared DbCountRunner");
//            }
//        };
//    }

    public static void main(String[] args) {
        SpringApplication.run(BookPubApplication.class, args);
    }

}
