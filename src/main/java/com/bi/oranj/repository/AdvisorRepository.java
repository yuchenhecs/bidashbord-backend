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

    @Query(value="SELECT advisor_id, advisor_first_name, advisor_last_name, GROUP_CONCAT(type separator ','), " +
            "COUNT(type) AS c FROM bi_goal WHERE firm_id = ?1 GROUP " +
            "BY advisor_id, advisor_first_name, advisor_last_name ORDER BY advisor_first_name LIMIT ?2, ?3", nativeQuery = true)
    public List<Object[]> findGoalsOrdered (long firmId, int start, int next);

}
