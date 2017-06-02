package com.bi.oranj.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by jaloliddinbakirov on 5/31/17.
 */
@Data
@Entity
@Table(name = "bi_goal")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

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

    private Long count;

    public BiGoal(){
        // For JPA to use
    }
}
