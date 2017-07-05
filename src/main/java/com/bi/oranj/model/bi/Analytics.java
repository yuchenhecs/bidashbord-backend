package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jaloliddinbakirov on 6/29/17.
 */
@Data
@Entity
@Table(name = "analytics")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Analytics {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "session_duration")
    private Integer sessionDuration;

    @Column (name = "session_start_date")
    private Date sessionStartDate;

    @Column (name = "role_id")
    private Long roleId;

}
