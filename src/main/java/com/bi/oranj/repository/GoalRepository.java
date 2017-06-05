package com.bi.oranj.repository;

import com.bi.oranj.entity.BiGoal;
import com.bi.oranj.entity.GoalEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 * returns next given num of records from db
 */
public interface GoalRepository extends JpaRepository<BiGoal, Integer> {

    @Query(value = "SELECT count(goalId) FROM BiGoal")
    public Integer totalGoals();

    @Query(value = "SELECT count(goalId) FROM BiGoal WHERE firmId = ?1")
    public Integer totalAdvisorGoals (long firmId);

    @Query(value = "SELECT count(goalId) FROM BiGoal WHERE firmId = ?1 and advisorId = ?2")
    public Integer totalClientGoals (long firmId, long advisorId);

}