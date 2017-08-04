package com.bi.oranj.model.bi;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jaloliddinbakirov on 8/3/17.
 */
@Data
public class Grid {
    private Integer x;
    private Integer y;
    private Integer height;
    private Integer width;
}
