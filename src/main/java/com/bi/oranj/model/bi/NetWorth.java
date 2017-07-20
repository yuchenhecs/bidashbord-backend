package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "networth")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class NetWorth {


    @Id
    @Column(name = "id", columnDefinition = "BIGINT")
    private BigInteger id;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "client_id", columnDefinition = "BIGINT")
    private BigInteger userId;

    @Column(name = "asset_value")
    private BigDecimal assetValue;

    @Column(name = "liability_value")
    private BigDecimal liabilityValue;

    @Column(name = "inserted_on")
    private Timestamp insertedOn = new Timestamp((new Date()).getTime());

    @Column(name = "last_updated_on")
    private Timestamp updatedOn = new Timestamp((new Date()).getTime());
}
