package com.bi.oranj.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * Created by jaloliddinbakirov on 5/23/17.
 */
@Entity
@Table(name = "goals1")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class GoalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "advisor_id")
    private int advisorId;

    @Column (name = "creation_date")
    private String creationDate;

    @Column (name = "firm_id")
    private int firmId;

    @Column (name = "goal_id")
    private int goalId;

    @Column (name = "name")
    private String name;

    @Column (name = "type")
    private String type;

    @Column (name = "user_id")
    private int userId;

    private int count;

    public GoalEntity(){}

    public GoalEntity(String name){
        this.setName(name);
    }

    public GoalEntity(String name, int count){
        this.name = name;
        this.count = count;
    }

    public GoalEntity(int count, String type){
        this.count = count;
        this.type = type;
    }

    public GoalEntity(int firmId, String type, int count){
        this.firmId = firmId;
        this.type = type;
        this.count = count;
    }

    public GoalEntity(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(int advisorId) {
        this.advisorId = advisorId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getFirmId() {
        return firmId;
    }

    public void setFirmId(int firmId) {
        this.firmId = firmId;
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
