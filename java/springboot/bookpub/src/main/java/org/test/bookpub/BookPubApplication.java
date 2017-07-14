package org.test.bookpub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookPubApplication {

//    @Bean
//    public StartupRunner2 schedulerRunner2() {
//        return new StartupRunner2();
//    }

    @Bean
    public StartupRunner schedulerRunner() {
        return new StartupRunner();
    }

    public static void main(String[] args) {
        SpringApplication.run(BookPubApplication.class, args);
    }

}
