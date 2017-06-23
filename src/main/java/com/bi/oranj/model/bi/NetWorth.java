package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.xml.internal.bind.v2.model.core.ID;
import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * Created by robertyuan on 6/22/17.
 */
@Entity
@Data
@Table(name = "net_worth")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetWorth {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long Id;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "value")
    private Double value;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "asset_value")
    private Double assetValue;

    @Column(name = "liability_value")
    private Double liabilityValue;
}
