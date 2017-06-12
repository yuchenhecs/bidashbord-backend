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
public class ParentAum {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

//    @ManyToOne (fetch = FetchType.LAZY, )
    @Column (name = "account_id", columnDefinition = "BIGINT")
    private BigInteger accountId;

    @Column (name = "portfolio_id", columnDefinition = "BIGINT")
    private BigInteger portfolioId;

    @Column (name = "client_id", columnDefinition = "BIGINT")
    private BigInteger clientId;

    @Column (name = "is_inactive")
    private Boolean isInactive;

    @Column (name = "updated_on")
    private Date updatedOn;
}
