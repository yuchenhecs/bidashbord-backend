package com.bi.oranj.model;

import lombok.Data;

/**
 * Created by harshavardhanpatil on 5/23/17.
 */
@Data
public class HelloWorld {

    private final long id;
    private final String content;

    public HelloWorld(long id, String content) {
        this.id = id;
        this.content = content;
    }
}
