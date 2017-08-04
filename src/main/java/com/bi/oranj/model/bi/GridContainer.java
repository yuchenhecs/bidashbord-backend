package com.bi.oranj.model.bi;

import com.bi.oranj.model.bi.Grid;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jaloliddinbakirov on 8/3/17.
 */
@Data
public class GridContainer {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long userId;
    private Grid goals;
    private Grid aum;
    private Grid netWorth;
    private Grid logins;
}
