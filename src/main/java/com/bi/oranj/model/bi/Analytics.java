package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by jaloliddinbakirov on 6/29/17.
 */
@Data
@Entity
@Table(name = "analytics")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Analytics {

    private Long id;
    private Long clientId;
    private Integer sessionDuration;
    private Date sessionStartDate;
    private Long roleId;
}
