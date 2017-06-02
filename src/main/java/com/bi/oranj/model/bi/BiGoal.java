package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by harshavardhanpatil on 5/24/17.
 */

/**
 * Model class bi_goal table
 */
@Data
@Entity
@Table(name = "goal")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BiGoal {

    @Id
    @Column(name = "goal_id")
    private Long goalId;

    @Column(name = "goal_name")
    private String name;
    private String type;

    private boolean deleted;

    @Column(name = "creation_date")
    private String creationDate;

    @Column(name = "advisor_id")
    private Long advisorId;

    @Column(name = "firm_id")
    private Long firmId;

    @Column(name = "user_id")
    private Long userId;

    public BiGoal(){
        // For JPA to use
    }
}
