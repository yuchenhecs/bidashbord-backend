package com.bi.oranj.model.bi;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by robertyuan on 6/22/17.
 */
@Data
public class NetWorthForAdvisor {

    private Long clientId;
    private String name;
    private BigDecimal absNet;
}
