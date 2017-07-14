package com.bi.oranj.repository.bi;

import com.bi.oranj.constant.ConstantQueries;
import com.bi.oranj.model.bi.GamificationCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GamificationRepository extends JpaRepository<GamificationCategories, Long> {

    @Query(value = ConstantQueries.GET_GAMIFICATION_SUMMARY_QUERY, nativeQuery = true)
    public GamificationCategories findByAdvisorIdAndDate(@Param("advisor") Long advisor, @Param("date") String date);

}
