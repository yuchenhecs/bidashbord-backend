package com.bi.oranj.model.oranj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OranjAum {

    @Id
    @Column (name = "asset_id")
    private Long assetId;

    @Column (name = "portfolio_id")
    private Long portfolioId;

    @Column (name = "client_id")
    private Long clientId;

    @Column (name = "ticker_name")
    private String tickerName;

    @Column (name = "asset_class")
    private String assetClass;

    @Column (name = "price")
    private BigDecimal price;

    @Column (name = "quantity")
    private Double quantity;

    @Column (name = "value")
    private BigDecimal value;

    @Column (name = "creation_date")
    private Date creationDate;

    @Column (name = "updated_on")
    private Date updatedOn;

    public OranjAum(){

    }

    public OranjAum(Long assetId, Long portfolioId, Long clientId, String tickerName, String assetClass, BigDecimal price,
                    Double quantity, BigDecimal value, Date creationDate, Date updatedOn){

        this.assetId = assetId;
        this.portfolioId = portfolioId;
        this.clientId = clientId;
        this.tickerName = tickerName;
        this.assetClass = assetClass;
        this.price = price;
        this.quantity = quantity;
        this.value = value;
        this.creationDate = creationDate;
        this.updatedOn = updatedOn;

    }


}
