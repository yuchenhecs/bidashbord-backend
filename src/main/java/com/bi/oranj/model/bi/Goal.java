package com.bi.oranj.model.bi;

import com.bi.oranj.serializer.GoalResponseSerializer;
import com.bi.oranj.model.bi.wrapper.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;


@JsonIgnoreProperties(value = { "handler", "hibernateLazyInitializer" })
@JsonSerialize(using = GoalResponseSerializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Goal {

    private int totalGoals;
    private int totalUsers;

    private int page;   //  page number
    private int count;  //  page count
    private boolean last;
    private Collection<? extends User> users;

    @JsonIgnore
    private String type;

    public Goal(){}

    public Goal(Collection<? extends User> users, String type){
        this.users = Collections.emptyList();
        this.type = type;
    }
}
