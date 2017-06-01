package com.bi.oranj.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

/**
 * Created by jaloliddinbakirov on 5/24/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class User {
    private int userId;
    private String name;
    private String firstName;
    private String lastName;
    private int total;
    private HashMap<String, Integer> goals;

    public User(){
        this.goals = new HashMap<>();
    }

    public User(int userId, String name, HashMap<String, Integer> goals, int total){
        this.userId = userId;
        this.name = name;
        this.goals = goals;
        this.total += total;
    }

    public User(int userId, String firstName, String lastName, HashMap<String, Integer> goals, int total){
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.goals = goals;
        this.total += total;
    }


    public User(int userId, String name, HashMap<String, Integer> goals){
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

    public HashMap<String, Integer> getGoals() {
        return goals;
    }

    public void setGoals(HashMap<String, Integer> goal) {
        this.goals = goal;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    public int getUserId() {
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
