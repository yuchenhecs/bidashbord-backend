package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AUMForFirm extends BaseData {

    private long totalAdvisors;
    private List<AdvisorAUM> advisors = new ArrayList<>();
}
