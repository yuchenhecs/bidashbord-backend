package com.bi.oranj.model.bi;

import com.bi.oranj.repository.bi.GoalRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoalSummary {

    private String type;
    private Long count;

    public GoalSummary(String type, Long count){
        this.type = type;
        this.count = count;
    }
}
