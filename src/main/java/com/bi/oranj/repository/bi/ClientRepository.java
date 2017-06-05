package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by harshavardhanpatil on 6/2/17.
 */
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "SELECT c.id clientId, c.client_first_name firstName, c.client_last_name lastName, GROUP_CONCAT(g.type separator ',') type, " +
            "COUNT(g.id) count FROM goals g " +
            "JOIN clients c ON g.client_id = c.id WHERE g.advisor_id = ?1 AND g.firm_id = ?2 GROUP BY c.id, c.client_first_name " +
            "ORDER BY firstName LIMIT ?3, ?4", nativeQuery = true)
    public List<Object[]> findGoalsOrderedByFirmByAdvisor (long firmId, long advisorId, int pageNum, int next);

    @Query (value = "SELECT c.id clientId, c.client_first_name firstName, c.client_last_name lastName, GROUP_CONCAT(g.type separator ',') type, " +
            "COUNT(g.id) count FROM goals g " +
            "JOIN clients c ON g.client_id = c.id WHERE g.advisor_id = ?1 GROUP BY c.id, c.client_first_name " +
            "ORDER BY firstName LIMIT ?2, ?3", nativeQuery = true)
    public List<Object[]> findGoalsOrderedByAdvisor (long advisorId, int pageNum, int next);

    @Query(value = "SELECT DISTINCT(clientId) FROM BiGoal WHERE firmId = ?1 AND advisorId = ?2")
    public List<Client> findDistinctByFirmByAdvisor(long firmId, long advisorId);


    @Query(value = "SELECT DISTINCT(clientId) FROM BiGoal WHERE advisorId = ?1")
    public List<Client> findDistinctByAdvisor(long advisorId);
}
