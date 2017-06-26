package com.bi.oranj.model.bi;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by robertyuan on 6/22/17.
 */
@Data
public class NetWorthForSummary {

    private String date;
    private BigDecimal clients;
    private BigDecimal absNet;
}
