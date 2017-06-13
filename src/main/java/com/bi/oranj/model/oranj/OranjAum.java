package com.bi.oranj.model.oranj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jaloliddinbakirov on 6/8/17.
 */
@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OranjAum {

    @Id
    @Column (name = "portfolio_id")
    private Long portfolioId;

    @Column (name = "client_id")
    private Long clientId;
    public OranjAum(){

    }
}
