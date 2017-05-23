package com.bi.oranj.controller;

import com.bi.oranj.model.HelloWorld;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by harshavardhanpatil on 5/23/17.
 */
@RestController
public class HelloWorldController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/hello")
    public HelloWorld greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new HelloWorld(counter.incrementAndGet(), String.format(template, name));
    }
}
