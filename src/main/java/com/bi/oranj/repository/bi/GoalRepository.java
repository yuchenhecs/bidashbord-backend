package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.BiGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 * returns next given num of records from db
 */
public interface GoalRepository extends JpaRepository<BiGoal, Integer> {

    @Query(value = "SELECT count(id) FROM BiGoal")
    public Integer totalGoals();

    @Query(value = "SELECT count(id) FROM BiGoal WHERE firmId = ?1")
    public Integer totalAdvisorGoals (long firmId);

    @Query(value = "SELECT count(id) FROM BiGoal WHERE advisorId = ?1")
    public Integer totalClientGoals (long advisorId);

}