package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.oranj.OranjGoal;
import com.bi.oranj.model.oranj.OranjNetWorth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

import static com.bi.oranj.constant.ConstantQueries.*;

public interface OranjNetWorthRepository extends JpaRepository<OranjNetWorth, Integer> {

//    @Query(value = GET_GOALS_FOR_GIVEN_DAY_QUERY, nativeQuery = true)
//    List<OranjGoal> FindByCreationDate(@Param("start") String start, @Param("end") String end);

    @Query(value = "select n.id, n.date, n.value, n.user_id, n.asset_value, n.liability_value " +
            "from networth_history n " +
                "inner join auth_user a " +
                    "ON n.user_id = a.id \n" +
            "where date <= :end limit 0, 100", nativeQuery = true)
    List<Object[]> FindNetWorthTillDate(@Param("end") String end);
}

