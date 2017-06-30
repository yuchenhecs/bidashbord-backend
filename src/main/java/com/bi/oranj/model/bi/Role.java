package com.bi.oranj.model.bi;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jaloliddinbakirov on 6/29/17.
 */
@Data
@Entity
@Table(name = "roles")
public class Role {
    private Long id;
    private String name;
}
