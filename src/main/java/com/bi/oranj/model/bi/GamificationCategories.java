package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "goals")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GamificationCategories {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "advisor_id")
    private Long advisorId;

    @Column(name = "aum")
    private Double aum;

    @Column(name = "net_worth")
    private Double netWorth;

    private Long hni;

    @Column(name = "conversion_rate")
    private Double conversionRate;

    @Column(name = "avg_conversion_time")
    private Double avgConversionTime;

    @Column(name = "retention_rate")
    private Double retentionRate;

    @Column(name = "weekly_client_login")
    private Long weeklyClientLogin;

    @Column(name = "qoq_aum_growth")
    private Double qoqAumGrowth;

    @Column(name = "qoq_net_worth")
    private Double qoqNetWorthGrowth;

    @Column(name = "clientele_growth")
    private Double clienteleGrowth;

    @Column(name = "update_date")
    private Timestamp updateDate;

    public GamificationCategories(){
    }
}
