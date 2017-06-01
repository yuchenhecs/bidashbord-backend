package com.bi.oranj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by harshavardhanpatil on 5/23/17.
 */
@SpringBootApplication()
@EnableScheduling
public class ApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(ApplicationInitializer.class);

    public static void main(String[] args) {
        SpringApplication.run(ApplicationInitializer.class, args);
    }
}
