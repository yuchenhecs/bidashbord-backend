package com.bi.oranj.wrapper.user;

import com.bi.oranj.serializer.UserSerializer;
import com.bi.oranj.wrapper.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */
@JsonSerialize(using = UserSerializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Firm extends User{

    public Firm (int firmId, String name, HashMap<String, Integer> goals, int total){
        super(firmId, name, goals, total);
    }
}
