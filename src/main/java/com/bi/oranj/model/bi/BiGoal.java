package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by harshavardhanpatil on 5/24/17.
 */

/**
 * Model class bi_goal table
 */
@Data
@Entity
@Table(name = "goals")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BiGoal {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "goal_name")
    private String name;
    private String type;

    @Column(name = "advisor_id")
    private Long advisorId;

    @Column(name = "firm_id")
    private Long firmId;

    @Column(name = "client_id")
    private Long clientId;

    private boolean deleted;

    @Column(name = "goal_creation_date")
    private Timestamp goalCreationDate;

    @Column(name = "inserted_on")
    private Timestamp insertedOn = new Timestamp((new Date()).getTime());

    @Column(name = "last_updated_on")
    private Timestamp updatedOn = new Timestamp((new Date()).getTime());

    public BiGoal(){
        // For JPA to use
    }
}
