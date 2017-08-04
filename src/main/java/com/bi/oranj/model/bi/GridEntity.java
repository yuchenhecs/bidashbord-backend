package com.bi.oranj.model.bi;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jaloliddinbakirov on 8/3/17.
 */
@Data
@Entity
@Table(name = "grid_config")
public class GridEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;
    private String goals;
    private String aum;

    @Column (name = "net_worth")
    private String netWorth;
    private String logins;
}
