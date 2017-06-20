package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "advisors")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Advisor {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "advisor_first_name")
    private String advisorFirstName;

    @Column(name = "advisor_last_name")
    private String advisorLastName;

    @Column(name = "firm_id")
    private Long firmId;

    @Column(name = "advisor_created_on")
    private Timestamp createdOn;

    private boolean active;

    public Advisor(){
    }
}
