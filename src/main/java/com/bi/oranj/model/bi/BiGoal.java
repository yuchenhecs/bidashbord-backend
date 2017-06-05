package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @Column(name = "user_id")
    private Long userId;

    private boolean deleted;

    @Column(name = "creation_date")
    private String creationDate;

    public BiGoal(){
        // For JPA to use
    }
}
