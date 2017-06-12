package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jaloliddinbakirov on 6/8/17.
 */
@Entity
@Data
@Table (name = "aum")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Aum extends ParentAum{
}
