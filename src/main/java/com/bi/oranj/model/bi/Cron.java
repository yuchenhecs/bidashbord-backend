package com.bi.oranj.model.bi;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by jaloliddinbakirov on 7/10/17.
 */
@Data
@Entity(name = "cron_monitoring")
public class Cron {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "error_message")
    private String errorMessage;

    @Column (name = "start_time")
    private Date startTime;

    @Column (name = "end_time")
    private Date endTime;

    public Cron(){}
}
