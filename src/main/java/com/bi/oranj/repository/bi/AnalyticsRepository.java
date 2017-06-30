package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by jaloliddinbakirov on 6/29/17.
 */
@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, Long>{
//
//    @Query (value = ":query", nativeQuery = true)
//    public void save(@Param("query") String query);

}
