package com.bi.oranj.model.bi;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by robertyuan on 6/22/17.
 */
@Data
public class NetWorthForSummary {

    private Date date;
    private Double clients;
    private Double absNet;
}
