package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.bi.PositionsHistory;
import com.bi.oranj.model.oranj.OranjPositionsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by jaloliddinbakirov on 6/12/17.
 */
public interface OranjPositionsHistoryRepository extends JpaRepository<OranjPositionsHistory, Integer> {


    @Query(value = "select id position_id, portfolio portfolio_id, ticker_name, asset_class, price, quantity, value amount, " +
            "insert_date creation_date, update_date updated_on from quovo_positions_history limit 0, 1000", nativeQuery = true)
    List<Object[]> fetchPositionsHistory();

}
