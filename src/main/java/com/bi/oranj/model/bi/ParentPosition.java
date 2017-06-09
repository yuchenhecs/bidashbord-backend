package com.bi.oranj.model.bi;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by jaloliddinbakirov on 6/9/17.
 */
@Data
@MappedSuperclass
public abstract class ParentPosition {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BIGINT")
    private BigInteger id;

    @Column (name = "position_id", columnDefinition = "BIGINT")
    private BigInteger positionId;

    @Column (name = "portfolio_id", columnDefinition = "BIGINT")
    private BigInteger portfolioId;

    @Column (name = "ticker_name")
    private String tickerName;

    @Column (name = "asset_class")
    private String assetClass;

    @Column (name = "price")
    private BigDecimal price;

    @Column (name = "currency_code")
    private String currencyCode;

    @Column (name = "creation_date")
    private Date creationDate;

    @Column (name = "updated_on")
    private Date updatedOn;
}
