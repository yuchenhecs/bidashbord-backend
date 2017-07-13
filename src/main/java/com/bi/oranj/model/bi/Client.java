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
@Table(name = "clients")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Client {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "client_first_name")
    private String clientFirstName;

    @Column(name = "client_last_name")
    private String clientLastName;

    @Column(name = "advisor_id")
    private Long advisorId;

    @Column(name = "firm_id")
    private Long firmId;

    @Column(name = "client_created_on")
    private Timestamp createdOn;

    private boolean active;

    @Column(name = "role_id")
    private Integer roleId;

    private boolean converted;

    @Column(name = "converted_date")
    private Timestamp convertedDate;

    public Client(){
    }
}
