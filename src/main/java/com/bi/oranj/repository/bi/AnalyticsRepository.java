package com.bi.oranj.repository.bi;

import com.bi.oranj.constant.ConstantQueries;
import com.bi.oranj.model.bi.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jaloliddinbakirov on 6/29/17.
 */
@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, Long>{

    @Query(value = ConstantQueries.GET_LOGIN_METRICS_FOR_ADMIN_QUERY, nativeQuery = true)
    public List<Object[]> findLoginMetricsForFirm(@Param("firm") Long firm, @Param("role") Long role, @Param("start") String start, @Param("end") String end);

    @Query(value = ConstantQueries.GET_LOGIN_METRICS_FOR_FIRM_QUERY, nativeQuery = true)
    public List<Object[]> findLoginMetricsForAdvisor(@Param("advisor") Long advisor, @Param("role") Long role, @Param("start") String start, @Param("end") String end);

    @Query(value = ConstantQueries.GET_LOGIN_METRICS_FOR_ADVISOR_QUERY, nativeQuery = true)
    public List<Object[]> findLoginMetricsForClient(@Param("client") Long client, @Param("role") Long role, @Param("start") String start, @Param("end") String end);

    @Query(value = ConstantQueries.GET_LOGIN_METRICS_FOR_SUMMARY_QUERY, nativeQuery = true)
    public List<Object[]> findLoginMetricsSummary(@Param("role") Long role, @Param("start") String start, @Param("end") String end);

}
