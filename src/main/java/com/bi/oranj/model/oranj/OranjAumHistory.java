package com.bi.oranj.model.oranj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class OranjAumHistory {

    @Id
    @Column (name = "account", columnDefinition = "BIGINT")
    private BigInteger accountId;

    @Column (name = "id", columnDefinition = "BIGINT")
    private BigInteger portfolioId;

    @Column (name = "oranj_user_id", columnDefinition = "BIGINT")
    private BigInteger clientId;

    @Column (name = "value")
    private BigDecimal amount;

    @Column (name = "is_inactive")
    private Boolean isInactive;

    @Column (name = "insert_date")
    private Date updatedOn;

    OranjAumHistory(){

    }

    OranjAumHistory(BigInteger accountId, BigInteger portfolioId, BigInteger clientId, BigDecimal amount, Boolean isInactive,
                    Date updatedOn){

        this.accountId = accountId;
        this.portfolioId = portfolioId;
        this.clientId = clientId;
        this.amount = amount;
        this.isInactive = isInactive;
        this.updatedOn = updatedOn;
    }
}
