package com.bi.oranj.repository;

import com.bi.oranj.entity.BiGoal;
import com.bi.oranj.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */

public interface ClientRepository extends JpaRepository<BiGoal, Integer> {

    @Query (value = "SELECT user_id, user_first_name, user_last_name, GROUP_CONCAT(type separator ','), " +
            "count(type) as c FROM bi_goal WHERE firm_id = ?1 and advisor_id = ?2 " +
            "GROUP BY user_id, user_first_name, user_last_name ORDER BY user_first_name LIMIT ?3, ?4", nativeQuery = true)
    public List<Object[]> findGoalsOrderedByFirmByAdvisor (long firmId, long advisorId, int pageNum, int next);

    @Query (value = "SELECT user_id, user_first_name, user_last_name, GROUP_CONCAT(type separator ','), " +
            "count(type) as c FROM bi_goal WHERE advisor_id = ?1 " +
            "GROUP BY user_id, user_first_name, user_last_name ORDER BY user_first_name LIMIT ?2, ?3", nativeQuery = true)
    public List<Object[]> findGoalsOrderedByAdvisor (long advisorId, int pageNum, int next);

    @Query(value = "SELECT DISTINCT(userId) FROM BiGoal WHERE firmId = ?1 AND advisorId = ?2")
    public List<BiGoal> findDistinctByFirmByAdvisor(long firmId, long advisorId);

    @Query(value = "SELECT DISTINCT(userId) FROM BiGoal WHERE advisorId = ?1")
    public List<BiGoal> findDistinctByAdvisor(long advisorId);
}
