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

    @Query (value = "SELECT c.id clientId, c.client_first_name firstName, c.client_last_name lastName, GROUP_CONCAT(g.type separator ',') type, " +
            "COUNT(g.goal_id) count FROM goals g " +
            "JOIN clients c ON g.client_id = c.id WHERE g.advisor_id = ?1 AND g.firm_id = ?2 GROUP BY c.id, c.client_first_name " +
            "ORDER BY firstName LIMIT ?3, ?4", nativeQuery = true)
    public List<Object[]> findGoalsOrderedByFirmByAdvisor (long firmId, long advisorId, int pageNum, int next);

    @Query (value = "SELECT c.id clientId, c.client_first_name firstName, c.client_last_name lastName, GROUP_CONCAT(g.type separator ',') type, " +
            "COUNT(g.goal_id) count FROM goals g " +
            "JOIN clients c ON g.client_id = c.id WHERE g.advisor_id = ?1 GROUP BY c.id, c.client_first_name " +
            "ORDER BY firstName LIMIT ?2, ?3", nativeQuery = true)
    public List<Object[]> findGoalsOrderedByAdvisor (long advisorId, int pageNum, int next);

    @Query(value = "SELECT DISTINCT(userId) FROM BiGoal WHERE firmId = ?1 AND advisorId = ?2")
    public List<BiGoal> findDistinctByFirmByAdvisor(long firmId, long advisorId);

    @Query(value = "SELECT DISTINCT(userId) FROM BiGoal WHERE advisorId = ?1")
    public List<BiGoal> findDistinctByAdvisor(long advisorId);
}
