package com.bi.oranj.model.bi;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jaloliddinbakirov on 6/9/17.
 */
@Data
@Entity
@Table (name = "aum_history")
public class AumHistory extends ParentAum{
}
