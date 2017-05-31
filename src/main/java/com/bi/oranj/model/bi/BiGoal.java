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
@Table(name = "bi_goal")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BiGoal {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(name = "goal_name")
    private String name;
    private String type;

    @Column(name = "goal_id")
    private Long goalId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "advisor_id")
    private Long advisorId;

    @Column(name = "firm_id")
    private Long firmId;

    @Column(name = "firm_name")
    private String firmName;

    private boolean deleted;

    @Column(name = "creation_date")
    private String creationDate;

    @Column(name = "user_first_name")
    private String userFirstName;

    @Column(name = "user_last_name")
    private String userLastName;

    @Column(name = "advisor_first_name")
    private String advisorFirstName;

    @Column(name = "advisor_last_name")
    private String advisorLastName;

    public BiGoal(){
        // For JPA to use
    }
}
