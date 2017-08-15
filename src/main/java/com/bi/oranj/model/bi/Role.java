package com.bi.oranj.model.bi;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jaloliddinbakirov on 6/29/17.
 */
@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    private Long id;

    private String name;
}
