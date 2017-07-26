package com.bi.oranj.config;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collection;

public class OranjAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final Object userId;

    public OranjAuthenticationToken(Object principal, Object credentials, Collection authorities, Object userId) {
        super(principal, credentials, authorities);
        this.userId = userId;
    }

    public Object getUserId() {
        return userId;
    }
}