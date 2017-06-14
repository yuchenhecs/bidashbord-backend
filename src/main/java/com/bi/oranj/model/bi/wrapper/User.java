package com.bi.oranj.model.bi.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaloliddinbakirov on 5/24/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class User {
    private long userId;
    private String name;
    private String firstName;
    private String lastName;
    private long total;
    private Map<String, Integer> goals;

    public User(){
        this.goals = new HashMap<>();
    }

    public User(long userId, String name, Map<String, Integer> goals, long total){
        this.userId = userId;
        this.name = name;
        this.goals = goals;
        this.total += total;
    }

    public User(long userId, String firstName, String lastName, Map<String, Integer> goals, long total){
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.goals = goals;
        this.total = total;
    }


    public User(long userId, String name, Map<String, Integer> goals){
        this.userId = userId;
        this.name = name;
        this.goals = goals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getGoals() {
        return goals;
    }

    public void setGoals(Map<String, Integer> goal) {
        this.goals = goal;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total += total;
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
