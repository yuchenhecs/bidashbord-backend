package com.bi.oranj.model.bi;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table (name = "aum_history")
public class AumHistory extends ParentAum{
}
