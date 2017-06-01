package com.bi.oranj.wrapper.user;

import com.bi.oranj.serializer.UserSerializer;
import com.bi.oranj.wrapper.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.jnlp.UnavailableServiceException;
import java.util.HashMap;

/**
 * Created by jaloliddinbakirov on 5/31/17.
 */
@JsonSerialize(using = UserSerializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Client extends User {

    public Client (int clientId, String name, HashMap<String, Integer> goals, int total){
        super(clientId, name, goals, total);
    }

}
