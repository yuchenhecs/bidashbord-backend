package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.oranj.OranjPositions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by jaloliddinbakirov on 6/13/17.
 */
public interface OranjPositionsRepository extends JpaRepository<OranjPositions, Integer>{

    @Query(value = "select qpos.id position_id, qport.id portfolio_id, qport.oranj_user_id client_id, qpos.ticker_name tickerName, " +
            "qpos.asset_class assetClass, qpos.price price, qpos.quantity quantity, qpos.value value, " +
            "qpos.insert_date creationDate, qpos.update_date updatedOn from quovo_portfolios qport " +
            "join quovo_positions qpos on qport.id = qpos.portfolio where oranj_user_id is not null", nativeQuery = true)
    List<Object[]> fetchPositionsData ();

}
