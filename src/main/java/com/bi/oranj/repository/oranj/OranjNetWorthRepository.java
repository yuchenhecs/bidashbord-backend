package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.oranj.OranjNetWorth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OranjNetWorthRepository extends JpaRepository<OranjNetWorth, Integer> {

    @Query(value = "select n.id, n.date, n.value, n.user_id, n.asset_value, n.liability_value " +
            "from networth_history n " +
                "inner join auth_user a " +
                    "ON n.user_id = a.id \n" +
            "where date(date) <= (:date)", nativeQuery = true)
    List<Object[]> findNetWorthTillDate(@Param("date") String date);


    @Query(value = "select n.id, n.date, n.value, n.user_id, n.asset_value, n.liability_value " +
            "from networth_history n " +
                "inner join auth_user a " +
                    "ON n.user_id = a.id \n" +
            "where date(date) IN (:date)", nativeQuery = true)
    List<Object[]> findByCreationDate(@Param("date") String date);



}

