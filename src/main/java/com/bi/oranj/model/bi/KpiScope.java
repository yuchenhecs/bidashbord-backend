package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by jaloliddinbakirov on 7/24/17.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KpiScope {
    private BigDecimal percentile;
    private BigDecimal best;
    private BigDecimal worst;
}
