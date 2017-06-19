package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

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

    public Firm(){
    }
}
