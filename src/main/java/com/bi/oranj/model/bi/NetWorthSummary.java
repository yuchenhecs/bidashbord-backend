package com.bi.oranj.model.bi;

import lombok.Data;

import java.util.Date;

/**
 * Created by robertyuan on 6/22/17.
 */
@Data
public class NetWorthSummary {
    private Date date;
    private Double clients;
    private Double absNet;

}
