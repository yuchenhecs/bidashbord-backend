package com.bi.oranj.repository.bi;

import com.bi.oranj.constant.ConstantQueries;
import com.bi.oranj.model.bi.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static com.bi.oranj.constant.ConstantQueries.*;

public interface AumRepository extends JpaRepository<Position, Long> {

    @Query(value = GET_AUM_FOR_ADVISOR_QUERY, nativeQuery = true)
    public List<Object[]> findAUMsForAdvisor(@Param("advisor") Long advisor, @Param("date") String date);

    @Query(value = ConstantQueries.GET_AUM_FOR_FIRM_QUERY, nativeQuery = true)
    public List<Object[]> findAUMsForFirm(@Param("firm") Long firm, @Param("date") String date);

    @Query(value = ConstantQueries.GET_AUM_FOR_ADMIN_QUERY, nativeQuery = true)
    public List<Object[]> findAUMsForAdmin(@Param("date") String date);

    @Query(value = GET_AUM_SUMMARY_QUERY, nativeQuery = true)
    public List<Object[]> findAUMsSummary(@Param("date") String date);

    @Query(value = GET_AUM_SUMMARY_ADVISOR_QUERY, nativeQuery = true)
    public List<Object[]> findAUMsSummaryForAdvisor(@Param("advisorId") Long advisorId,
                                                    @Param("date") String date);

    @Query(value = GET_AUM_SUMMARY_FIRM_QUERY, nativeQuery = true)
    public List<Object[]> findAUMsSummaryForFirm(@Param("firmId") Long firmId,
                                                 @Param("date") String date);
}