package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by jaloliddinbakirov on 7/24/17.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdvisorPerformance {
    private KpiScope overall;
    private KpiScope state;
    private KpiScope firm;
    private BigDecimal advisorKpi;
    private String stateCode;
}
