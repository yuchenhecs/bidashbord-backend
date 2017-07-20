package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gamification_pat_on_the_back")
public class PatOnTheBack {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "advisor_id")
    private Long advisorId;

    private String region;

    @Column(name = "aum")
    private String aumAchievement;

    @Column(name = "net_worth")
    private String netWorthAchievement;

    @Column(name = "hni")
    private String hniAchievement;

    @Column(name = "conversion_rate")
    private String conversionRateAchievement;

    @Column(name = "avg_conversion_time")
    private String avgConversionRateAchievement;

    @Column(name = "retention_rate")
    private String retentionRateAchievement;

    @Column(name = "weekly_logins")
    private String weeklyClientLoginsAchievement;

    @Column(name = "aum_growth")
    private String aumGrowthAchievement;

    @Column(name = "net_worth_growth")
    private String netWorthGrowthAchievement;

    @Column(name = "clientele_growth")
    private String clienteleGrowthAchievement;
}
