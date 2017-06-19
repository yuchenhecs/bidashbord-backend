package com.bi.oranj.model.bi.wrapper.user;

import com.bi.oranj.serializer.UserSerializer;
import com.bi.oranj.model.bi.wrapper.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(using = UserSerializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Firm extends User{

    public Firm (long firmId, String name, Map<String, Integer> goals, long total){
        super(firmId, name, goals, total);
    }
}
