package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.oranj.OranjAum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface OranjAUMRepository extends JpaRepository <OranjAum, Long>{
    @Query (value = "select id portfolio_id, oranj_user_id client_id " +
            "from quovo_portfolios where oranj_user_id is not null and id is not null", nativeQuery = true)
    List<Object[]> fetchPortfolioClientMapping();

    @Query (value = "select id portfolio_id, oranj_user_id client_id " +
            "from quovo_portfolios where oranj_user_id = :clientId", nativeQuery = true)
    List<Object[]> fetchPortfolioClientMappingByClient(@Param("clientId") Long clientId);

    @Query (value = "select qpos.id assetId, qport.id portfolioId, qport.oranj_user_id clientId, qpos.ticker_name tickerName, " +
            "qpos.asset_class assetClass, qpos.price price, qpos.quantity quantity, qpos.value value, qpos.insert_date positionCreationDate, " +
            "qpos.update_date positionUpdatedOn, qport.is_inactive, qport.account, qport.update_date aum_update " +
            "from quovo_portfolios qport\n" +
            "join quovo_positions qpos on qport.id = qpos.portfolio where oranj_user_id is not null ", nativeQuery = true)
    List<Object[]> fetchAUMData ();
}
