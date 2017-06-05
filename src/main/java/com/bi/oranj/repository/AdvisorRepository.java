package com.bi.oranj.repository;

import com.bi.oranj.entity.BiGoal;
import com.bi.oranj.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by jaloliddinbakirov on 5/24/17.
 */
public interface AdvisorRepository extends JpaRepository<BiGoal, Integer>{

    @Query(value = "SELECT DISTINCT(advisorId) FROM BiGoal WHERE firmId = ?1")
    public List<BiGoal> findDistinct(long firmId);

    @Query (value = "SELECT a.id advisorId, a.advisor_first_name firstName, a.advisor_last_name lastName, GROUP_CONCAT(g.type separator ',') type, " +
            "COUNT(g.goal_id) count FROM goals g " +
            "JOIN advisors a ON g.advisor_id = a.id WHERE g.firm_id = ?1 GROUP BY a.id, a.advisor_first_name " +
            "ORDER BY firstName LIMIT ?2, ?3", nativeQuery = true)
    public List<Object[]> findGoalsOrdered (long firmId, int start, int next);

}
