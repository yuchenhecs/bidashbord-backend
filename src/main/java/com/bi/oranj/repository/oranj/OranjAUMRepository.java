package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.oranj.OranjAum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by jaloliddinbakirov on 6/8/17.
 */
public interface OranjAUMRepository extends JpaRepository <OranjAum, Long>{
    @Query (value = "select id portfolio_id, oranj_user_id client_id " +
            "from quovo_portfolios where oranj_user_id is not null and id is not null", nativeQuery = true)
    List<Object[]> fetchPortfolioClientMapping();

    @Query (value = "select id portfolio_id, oranj_user_id client_id " +
            "from quovo_portfolios where oranj_user_id = :clientId", nativeQuery = true)
    List<Object[]> fetchPortfolioClientMappingByClient(@Param("clientId") Long clientId);

}
