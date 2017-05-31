package com.bi.oranj.model.oranj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by harshavardhanpatil on 5/24/17.
 */

/**
 * Model class user_goal table
 */
@Entity
@Data
@Table(name = "user_goal")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OranjGoal {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String name;
    private String priority;
    private String type;

    @Column(name = "user_id", nullable = true)
    private Long user;

    @Column(name = "creation_date", nullable = true)
    private String creationDate;

    @Column(name = "created_by_id", nullable = true)
    private Long createdById;

    @Column(name = "last_update", nullable = true)
    private String lastUpdate;

    @Column(name = "updated_by_id", nullable = true)
    private Long updatedById;
    private boolean deleted;

    public OranjGoal(){
        // For JPA to use
    }
}
