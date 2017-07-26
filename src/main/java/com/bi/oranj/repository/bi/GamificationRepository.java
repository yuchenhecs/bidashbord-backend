package com.bi.oranj.repository.bi;

import com.bi.oranj.constant.ConstantQueries;
import com.bi.oranj.model.bi.GamificationCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.security.PermitAll;
import javax.validation.OverridesAttribute;
import java.math.BigDecimal;
import java.util.List;

public interface GamificationRepository extends JpaRepository<GamificationCategories, Long> {

    @Query(value = ConstantQueries.GET_GAMIFICATION_QUERY, nativeQuery = true)
    public List<Object[]> findByAdvisorIdAndDate(@Param("advisor") Long advisor, @Param("date") String date);

    @Transactional
    @Query(value = "call max_min_kpi(:kpiName, :scope, :advisorId, :date)", nativeQuery = true)
    public List<Object[]> findMaxAndMinInTheGivenKpi (
                                    @Param("kpiName") String kpiName,
                                    @Param("scope") String scope,
                                    @Param("advisorId") Long advisorId,
                                    @Param("date") String date);



    @Transactional
    @Query(value = "call advisor_kpi_percentile (:advisorId, :kpi, :date, :scope)", nativeQuery = true)
    public BigDecimal findAdvisorKpiPercentile (@Param("advisorId") Long advisorId, @Param("kpi") String kpi,
                                                @Param("date") String date, @Param("scope") String scope);
}
