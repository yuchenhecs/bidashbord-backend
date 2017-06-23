package com.bi.oranj.model.bi;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by robertyuan on 6/22/17.
 */
@Data
public class NetWorthForFirm {

    private Long Id;
    private String firstName;
    private String lastName;
    private BigDecimal absNet;
    private BigDecimal avgNet;
}
