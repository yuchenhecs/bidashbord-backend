package com.bi.oranj.model.bi;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by robertyuan on 6/22/17.
 */
@Data
public class NetWorthForAdmin {

    private Long firmId;
    private String name;
    private BigDecimal absNet;
    private BigDecimal avgNet;
}
