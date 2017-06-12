package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jaloliddinbakirov on 6/9/17.
 */
@Data
@Entity
@Table (name = "positions_history")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionsHistory extends ParentPosition {
}
