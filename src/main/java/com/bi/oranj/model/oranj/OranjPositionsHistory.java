package com.bi.oranj.model.oranj;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by jaloliddinbakirov on 6/12/17.
 */

@Data
@Entity
public class OranjPositionsHistory {

    @Id
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

    @Column (name = "quantity")
    private Double quantity;

    @Column (name = "amount")
    private BigDecimal amount;

    @Column (name = "currency_code")
    private String currencyCode;

    @Column (name = "creation_date")
    private Date creationDate;

    @Column (name = "updated_on")
    private Date updatedOn;

    OranjPositionsHistory (BigInteger positionId, BigInteger portfolioId, String tickerName, String assetClass,
                           BigDecimal price, Double quantity, BigDecimal amount, String currencyCode, Date creationDate,
                           Date updatedOn){

        this.positionId = positionId;
        this.portfolioId = portfolioId;
        this.tickerName = tickerName;
        this.assetClass = assetClass;
        this.price = price;
        this.quantity = quantity;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.creationDate = creationDate;
        this.updatedOn = updatedOn;
    }

    OranjPositionsHistory (){

    }

}
