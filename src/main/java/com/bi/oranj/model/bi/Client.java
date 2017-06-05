package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by harshavardhanpatil on 6/2/17.
 */
@Data
@Entity
@Table(name = "user_table")
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

    public Client(){
    }
}
