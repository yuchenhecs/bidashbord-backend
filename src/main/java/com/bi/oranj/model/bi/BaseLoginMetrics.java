package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseLoginMetrics {

    private String name;
	private Long totalLogins;
    private Long uniqueLogins;
    private Double avgSessionTime;
}
