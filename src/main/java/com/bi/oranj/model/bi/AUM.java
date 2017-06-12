package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by harshavardhanpatil on 6/9/17.
 */
@Data
@Entity
@Table(name = "aum")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AUM {

    @Id
    private Long id;

    @Column(name = "position_id")
    private Long positionId;

    @Column(name = "portfolio_id")
    private Long portfolioId;

    @Column(name = "client_id")
    private Long clientId;

    private double quantity;


}
