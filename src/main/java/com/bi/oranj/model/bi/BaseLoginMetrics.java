package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseLoginMetrics {

    private String name;
	private BigInteger totalLogins;
    private Long uniqueLogins;
    private BigDecimal avgSessionTime;
}
