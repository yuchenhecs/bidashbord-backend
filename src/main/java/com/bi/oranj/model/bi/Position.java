package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.scene.Parent;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jaloliddinbakirov on 6/9/17.
 */

@Entity
@Data
@Table(name = "positions")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Position extends ParentPosition{
}
