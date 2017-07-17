package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class Gamification {

    private Long advisorId;
    private String name;
    private Long points;
    private BigDecimal percentileOverall;
    private BigDecimal percentileState;
    private BigDecimal percentileFirm;
    private BigDecimal aum;
    private BigDecimal netWorth;
    private Integer hni;
    private BigDecimal conversionRate;
    private BigDecimal avgConversionTime;
    private BigDecimal retentionRate;
    private Integer weeklyLogins;
    private BigDecimal aumGrowth;
    private BigDecimal netWorthGrowth;
    private BigDecimal clienteleGrowth;
}
