package com.bi.oranj.model.draft;

import com.bi.oranj.wrapper.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Firm extends User{

    private int firmId;
    private String name;
    private int total;
    private HashMap<String, Integer> goals;

    public Firm(){
        this.goals = new HashMap<>();
    }

    public Firm(int firmId, String name, HashMap<String, Integer> goals, int total){
        this.firmId = firmId;
        this.name = name;
        this.goals = goals;
        this.total += total;
    }

    public Firm(int firmId, String name, HashMap<String, Integer> goals){
        this.firmId = firmId;
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

    public void setGoalEntities(HashMap<String, Integer> goal) {
        this.goals = goal;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total += total;
    }

    public int getFirmId() {
        return firmId;
    }

    public void setFirmId(int firmId) {
        this.firmId = firmId;
    }

}
