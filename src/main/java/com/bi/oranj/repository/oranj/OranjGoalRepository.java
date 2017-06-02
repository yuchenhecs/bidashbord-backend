package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.oranj.OranjGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static com.bi.oranj.constant.ConstantQuery.GET_GOALS_QUERY;

/**
 * Created by harshavardhanpatil on 5/24/17.
 */
public interface OranjGoalRepository extends JpaRepository<OranjGoal, Integer> {

    @Query(value = GET_GOALS_QUERY, nativeQuery = true)
    List<OranjGoal> FindByCreationDate(@Param("start") String start, @Param("end") String end);

    List <OranjGoal> findByCreationDateBetween(String start, String end);
}
