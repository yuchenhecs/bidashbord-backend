package com.bi.oranj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableScheduling
public class ApplicationInitializer {
//public class ApplicationInitializer extends SpringBootServletInitializer {

    private static final Logger log = LoggerFactory.getLogger(ApplicationInitializer.class);

    public static void main(String[] args) {
        log.info("Starting the Application");
        SpringApplication.run(ApplicationInitializer.class, args);
    }

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(ApplicationInitializer.class);
//    }
}
