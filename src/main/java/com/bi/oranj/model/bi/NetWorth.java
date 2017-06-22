package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.xml.internal.bind.v2.model.core.ID;
import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Created by robertyuan on 6/22/17.
 */
@Entity
@Data
@Table(name = "netWorth")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetWorth {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private BigInteger Id;

    @Column(name = "date")
    private String date;

    @Column(name = "value")
    private BigInteger value;

    @Column(name = "user_id")
    private BigInteger userId;

    @Column(name = "asset_value")
    private BigInteger assetValue;

    @Column(name = "liability_value")
    private BigInteger liabilityValue;
}
