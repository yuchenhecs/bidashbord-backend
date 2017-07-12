package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.oranj.OranjGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static com.bi.oranj.constant.ConstantQueries.*;

public interface OranjGoalRepository extends JpaRepository<OranjGoal, Integer> {

    @Query(value = GET_GOALS_FOR_GIVEN_DAY_QUERY, nativeQuery = true)
    List<OranjGoal> findByCreationDate(@Param("date") String date);

    @Query(value = GET_GOALS_TILL_DATE_QUERY, nativeQuery = true)
    List<OranjGoal> findGoalsTillDate(@Param("date") String date);

    @Query(value = GET_ALL_FIRMS_QUERY, nativeQuery = true)
    List<Object[]> findAllFirms();

    @Query(value = GET_ALL_ADVISORS_QUERY, nativeQuery = true)
    List<Object[]> findAllAdvisors();

    @Query(value = GET_ALL_CLIENTS, nativeQuery = true)
    List<Object[]> findAllClients();

    @Query(value = GET_ALL_CLIENTS_WHO_ARE_ADVISORS_QUERY, nativeQuery = true)
    List<Object[]> findAllClientsWhoAreAdvisors();
}

