package com.bi.oranj.model;

import javax.persistence.*;

/**
 * Created by jaloliddinbakirov on 5/23/17.
 */
@Entity
@Table(name = "goals")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    public Goal(){}

    public Goal(String name){
        this.setName(name);
    }

    public Goal(long id){
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
}
