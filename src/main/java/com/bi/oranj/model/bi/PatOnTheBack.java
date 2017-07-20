package com.bi.oranj.model.bi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(name = "region", columnDefinition = "enum('STATE','OVERALL','FIRM')")
    private String region;

    @Column(name = "aum", columnDefinition = "enum('RANK_ONE', 'RANK_TWO', 'RANK_THREE', 'TOP_X', 'ABOVE_AVG', 'NO_RANK')")
    private String aumAchievement;

    @Column(name = "net_worth", columnDefinition = "enum('RANK_ONE', 'RANK_TWO', 'RANK_THREE', 'TOP_X', 'ABOVE_AVG', 'NO_RANK')")
    private String netWorthAchievement;

    @Column(name = "hni", columnDefinition = "enum('RANK_ONE', 'RANK_TWO', 'RANK_THREE', 'TOP_X', 'ABOVE_AVG', 'NO_RANK')")
    private String hniAchievement;

    @Column(name = "conversion_rate", columnDefinition = "enum('RANK_ONE', 'RANK_TWO', 'RANK_THREE', 'TOP_X', 'ABOVE_AVG', 'NO_RANK')")
    private String conversionRateAchievement;

    @Column(name = "avg_conversion_time", columnDefinition = "enum('RANK_ONE', 'RANK_TWO', 'RANK_THREE', 'TOP_X', 'ABOVE_AVG', 'NO_RANK')")
    private String avgConversionRateAchievement;

    @Column(name = "retention_rate", columnDefinition = "enum('RANK_ONE', 'RANK_TWO', 'RANK_THREE', 'TOP_X', 'ABOVE_AVG', 'NO_RANK')")
    private String retentionRateAchievement;

    @Column(name = "weekly_logins", columnDefinition = "enum('RANK_ONE', 'RANK_TWO', 'RANK_THREE', 'TOP_X', 'ABOVE_AVG', 'NO_RANK')")
    private String weeklyClientLoginsAchievement;

    @Column(name = "aum_growth", columnDefinition = "enum('RANK_ONE', 'RANK_TWO', 'RANK_THREE', 'TOP_X', 'ABOVE_AVG', 'NO_RANK')")
    private String aumGrowthAchievement;

    @Column(name = "net_worth_growth", columnDefinition = "enum('RANK_ONE', 'RANK_TWO', 'RANK_THREE', 'TOP_X', 'ABOVE_AVG', 'NO_RANK')")
    private String netWorthGrowthAchievement;

    @Column(name = "clientele_growth", columnDefinition = "enum('RANK_ONE', 'RANK_TWO', 'RANK_THREE', 'TOP_X', 'ABOVE_AVG', 'NO_RANK')")
    private String clienteleGrowthAchievement;
}
