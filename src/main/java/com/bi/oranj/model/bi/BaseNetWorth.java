package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by robertyuan on 6/22/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseNetWorth {

    private boolean hasNext;
    private int page;
}
