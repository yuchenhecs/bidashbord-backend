package com.bi.oranj.model.bi.wrapper.user;

import com.bi.oranj.serializer.UserSerializer;
import com.bi.oranj.model.bi.wrapper.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(using = UserSerializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Client extends User {

    public Client (int clientId, String firstName, String lastName, Map<String, Integer> goals, int total){
        super(clientId, firstName, lastName, goals, total);
    }

    public Client (int clientId, String name, Map<String, Integer> goals, int total){
        super(clientId, name, goals, total);
    }

}
