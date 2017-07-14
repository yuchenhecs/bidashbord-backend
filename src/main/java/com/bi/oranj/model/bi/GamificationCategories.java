package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "gamification_categories")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GamificationCategories {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "advisor_id")
    private Long advisorId;

    @Column(name = "aum")
    private BigDecimal aum;

    @Column(name = "net_worth")
    private BigDecimal netWorth;

    private Integer hni;

    @Column(name = "conversion_rate")
    private BigDecimal conversionRate;

    @Column(name = "avg_conversion_time")
    private BigDecimal avgConversionTime;

    @Column(name = "retention_rate")
    private BigDecimal retentionRate;

    @Column(name = "weekly_logins")
    private Integer weeklyLogins;

    @Column(name = "aum_growth")
    private BigDecimal aumGrowth;

    @Column(name = "net_worth_growth")
    private BigDecimal netWorthGrowth;

    @Column(name = "clientele_growth")
    private BigDecimal clienteleGrowth;

    @Column(name = "update_date")
    private Timestamp updateDate;

    public GamificationCategories(){
    }
}
