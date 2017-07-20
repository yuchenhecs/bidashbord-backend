package com.bi.oranj.repository.bi;

import com.bi.oranj.constant.ConstantQueries;
import com.bi.oranj.model.bi.PatOnTheBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatOnTheBackRepository extends JpaRepository<PatOnTheBack, Long>{

    @Query(value = ConstantQueries.PAT_ON_THE_BACK_QUERY, nativeQuery = true)
    public List<Object[]> findByAdvisorIdRegionAndDate(@Param("advisor") Long advisor, @Param("region") String region, @Param("date") String date);
}
