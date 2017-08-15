package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginMetricsForAdmin extends BaseData {

    private String unit;
    private long totalFirms;
    private List<FirmLoginMetrics> firms = new ArrayList<>();
}
