package com.bi.oranj.model.bi.wrapper.user;

import com.bi.oranj.serializer.UserSerializer;
import com.bi.oranj.model.bi.wrapper.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;

/**
 * Created by jaloliddinbakirov on 5/24/17.
 */
@JsonSerialize(using = UserSerializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Advisor extends User{

    public Advisor (int advisorId, String firstName, String lastName, HashMap<String, Integer> goals, int total){
        super(advisorId, firstName, lastName, goals, total);
    }
}
