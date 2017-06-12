package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by harshavardhanpatil on 6/12/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AumDiff {

    private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private BigDecimal total;
    private Map<String, BigDecimal> assetClass = new HashMap<>();
}
