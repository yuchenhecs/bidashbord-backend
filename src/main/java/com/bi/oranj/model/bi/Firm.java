package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "firms")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Firm {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "firm_name")
    private String firmName;

    @Column(name = "firm_created_on")
    private Timestamp createdOn;

    @Column(name = "active")
    private boolean active;

    @Column(name = "state")
    private String state;



    public Firm(){
    }
}
