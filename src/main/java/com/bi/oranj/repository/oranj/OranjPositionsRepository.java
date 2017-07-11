package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.oranj.OranjPositions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


    @Query(value = "select poshis.id position_id, poshis.portfolio portfolio_id, qport.oranj_user_id client_id, " +
            "poshis.ticker_name, poshis.asset_class, poshis.price, poshis.quantity, poshis.value amount, " +
            "poshis.insert_date creation_date, poshis.update_date updated_on from quovo_positions_history poshis " +
            "join quovo_portfolios qport on qport.id = poshis.portfolio", nativeQuery = true)
    List<Object[]> fetchPositionsHistory();

    @Query (value = "select poshis.id position_id, poshis.portfolio portfolio_id, qport.oranj_user_id client_id, " +
            "poshis.ticker_name, poshis.asset_class, poshis.price, poshis.quantity, poshis.value amount, " +
            "poshis.insert_date creation_date, poshis.update_date updated_on from quovo_positions_history poshis " +
            "join quovo_portfolios qport on qport.id = poshis.portfolio limit 0, :num", nativeQuery = true)
    List<Object[]> fetchPositionsHistoryWithLimit(@Param("num") Long num);

    @Query (value = "select poshis.id position_id, poshis.portfolio portfolio_id, qport.oranj_user_id client_id, " +
            "poshis.ticker_name, poshis.asset_class, poshis.price, poshis.quantity, poshis.value amount, " +
            "poshis.insert_date creation_date, poshis.update_date updated_on from quovo_positions_history poshis " +
            "join quovo_portfolios qport on qport.id = poshis.portfolio " +
            "where poshis.update_date between ':date 00:00:00' and ':date 23:59:59'", nativeQuery = true)
    List<OranjPositions> fetchPositionsHistoryByDate(@Param("date") String date);

}
