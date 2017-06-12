package com.bi.oranj.model.oranj;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jaloliddinbakirov on 6/12/17.
 */
@Data
@Entity
@Table (name = "auth_user")
public class OranjClient {


    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String clientFirstName;

    @Column(name = "last_name")
    private String clientLastName;

    @Column(name = "advisor_id")
    private Long advisorId;

    @Column(name = "firm_id")
    private Long firmId;

    private String advisorFirstName;
    private String advisorLastName;
    private String firmName;


    public OranjClient(){
    }
}
