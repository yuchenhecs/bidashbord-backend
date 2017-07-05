package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginMetricsSummary {

    private BigDecimal totalLogins;
    private BigDecimal uniqueLogins;
    private BigDecimal avgSessionTime;
    private BigDecimal changeInTotalLogins;
    private BigDecimal changeInUniqueLogins;
    private BigDecimal changeInAvgSessionTime;
}
