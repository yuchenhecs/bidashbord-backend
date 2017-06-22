package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.oranj.OranjGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

import static com.bi.oranj.constant.ConstantQueries.*;

public interface OranjGoalRepository extends JpaRepository<OranjGoal, Integer> {

    @Query(value = GET_GOALS_FOR_GIVEN_DAY_QUERY, nativeQuery = true)
    List<OranjGoal> FindByCreationDate(@Param("start") String start, @Param("end") String end);

    @Query(value = GET_GOALS_TILL_DATE_QUERY, nativeQuery = true)
    List<OranjGoal> FindGoalsTillDate(@Param("end") String end);

    List <OranjGoal> findByCreationDateBetween(String start, String end);

    @Query (value = GET_CLIENT_AND_GOAL_INFO, nativeQuery = true)
    List<OranjGoal> findByClientId (@Param("id") BigInteger id);

    @Query(value = GET_ALL_FIRMS_QUERY, nativeQuery = true)
    List<Object[]> FindAllFirms();

    @Query(value = GET_ALL_ADVISORS_QUERY, nativeQuery = true)
    List<Object[]> FindAllAdvisors();

    @Query(value = GET_ALL_CLIENTS_QUERY, nativeQuery = true)
    List<Object[]> FindAllClients();

    @Query(value = GET_ALL_CLIENTS_WHO_ARE_ADVISORS_QUERY, nativeQuery = true)
    List<Object[]> FindAllClientsWhoAreAdvisors();
}

