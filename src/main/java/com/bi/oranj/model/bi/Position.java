package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.scene.Parent;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
@Table(name = "positions")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Position {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BIGINT")
    private BigInteger id;

    @Column (name = "position_id", columnDefinition = "BIGINT")
    private BigInteger positionId;

    @Column (name = "portfolio_id", columnDefinition = "BIGINT")
    private BigInteger portfolioId;

    @Column (name = "client_id", columnDefinition = "BIGINT")
    private BigInteger clientId;

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

    @Column (name = "position_creation_date")
    private Date creationDate;

    @Column (name = "position_updated_on")
    private Date updatedOn;
}
