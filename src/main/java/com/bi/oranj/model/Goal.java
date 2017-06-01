package com.bi.oranj.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Goal {
    private int goal_id;
    private String name;
    private String type;
    private int count;

    public int getGoal_id() {
        return goal_id;
    }

    public void setGoal_id(int goal_id) {
        this.goal_id = goal_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
