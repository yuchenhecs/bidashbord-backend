package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table (name = "aum")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Aum extends ParentAum{
}
