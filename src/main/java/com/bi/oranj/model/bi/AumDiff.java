package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AumDiff {

    private String date;
    private BigDecimal total;
    private Map<String, BigDecimal> assetClass = new HashMap<>();

    public AumDiff(){
    }

    public AumDiff(String date, BigDecimal total, Map<String, BigDecimal> assetClass){
        this.date = date;
        this.total = total;
        this.assetClass = assetClass;
    }
}
