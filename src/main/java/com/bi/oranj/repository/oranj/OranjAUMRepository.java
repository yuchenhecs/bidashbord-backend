package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.oranj.OranjAum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by jaloliddinbakirov on 6/8/17.
 */
public interface OranjAUMRepository extends JpaRepository <OranjAum, Long>{

    @Query (value = "select qpos.id assetId, qport.id portfolioId, qport.oranj_user_id clientId, qpos.ticker_name tickerName, " +
            "qpos.asset_class assetClass, qpos.price price, qpos.quantity quantity, qpos.value value, qpos.insert_date creationDate, " +
            "qpos.update_date updatedOn, qport.is_inactive, qport.account, qport.update_date aum_update " +
            "from quovo_portfolios qport\n" +
            "join quovo_positions qpos on qport.id = qpos.portfolio where oranj_user_id is not null ", nativeQuery = true)
    List<Object[]> fetchAUMData ();

//    @Query (value = "")
//    List<Object[]> fetchPositionsHistory ();
//
//    @Query (value = "")
//    List<Object[]> fetchPortfolioHistory();
}
