package com.bi.oranj.repository.bi;

import com.bi.oranj.constant.ConstantQueries;
import com.bi.oranj.model.bi.GamificationAdvisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GamificationRankRepository extends JpaRepository<GamificationAdvisor, Long>{

    @Query(value = ConstantQueries.GET_GAMIFICATION_RANK_QUERY, nativeQuery = true)
    public GamificationAdvisor findByAdvisorIdAndDate(@Param("advisor") Long advisor, @Param("date") String date);

}
