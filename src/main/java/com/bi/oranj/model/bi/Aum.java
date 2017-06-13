package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by jaloliddinbakirov on 6/8/17.
 */
@Entity
@Data
@Table (name = "aum")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Aum {

    @Id
    @Column (name = "portfolio_id", columnDefinition = "BIGINT")
    private BigInteger portfolioId;

    @Column (name = "client_id", columnDefinition = "BIGINT")
    private BigInteger clientId;
}
