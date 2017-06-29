package com.bi.oranj.model.oranj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class networth_history table
 */
@Entity
@Data
@Table(name = "networth_history")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OranjNetWorth {

    @Id
    @Column(name = "id")
    private BigInteger id;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "value")
    private double value;

    @Column(name = "user_id")
    private BigInteger userId;

    @Column(name = "asset_value")
    private double assetValue;

    @Column(name = "liability_value")
    private double liabilityValue;

    public OranjNetWorth(){
        // For JPA to use
    }
}
