package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by harshavardhanpatil on 6/2/17.
 */
public interface ClientRepository extends JpaRepository<Client, Long> {


    @Query (value = "SELECT c.id clientId, c.client_first_name firstName, c.client_last_name lastName, g.type type, " +
            "COUNT(g.id) count FROM clients c " +
            "LEFT JOIN goals g ON g.client_id = c.id WHERE c.advisor_id = ?1 GROUP BY c.id, g.type, c.client_first_name " +
            "ORDER BY firstName LIMIT ?2, ?3", nativeQuery = true)
    public List<Object[]> findGoalsOrderedByAdvisor (long advisorId, int pageNum, int next);


    @Query(value = "SELECT DISTINCT(clientId) FROM BiGoal WHERE advisorId = ?1")
    public List<Client> findDistinctByAdvisor(long advisorId);

    @Query(value = "SELECT DISTINCT(id) from clients WHERE advisor_id = ?1", nativeQuery = true)
    public List<Object[]> findDistinctByAdvisorFromClients(long advisorId);


    @Query (value = "SELECT c.id clientId, c.client_first_name firstName, c.client_last_name lastName, g.type type, " +
            "COUNT(g.id) count FROM clients c " +
            "LEFT JOIN goals g ON g.client_id = c.id " +
            "WHERE c.advisor_id = :advisorId " +
            "GROUP BY c.id, g.type, c.client_first_name " +
            "ORDER BY firstName LIMIT :start, :next", nativeQuery = true)
    public List<Object[]> findGoalsOrdered (@Param("advisorId") Long advisorId,
                                            @Param("start") int start,
                                            @Param("next") int next);


    @Query (value = "SELECT c.id clientId, c.client_first_name firstName, c.client_last_name lastName, g.type type, " +
            "COUNT(g.id) count FROM clients c " +
            "LEFT JOIN goals g ON g.client_id = c.id " +
            "WHERE c.advisor_id = :advisorId " +
            "AND g.creation_date BETWEEN ':startDate 00:00:00' and ':endDate 23:59:59' " +
            "GROUP BY c.id, g.type, c.client_first_name " +
            "ORDER BY firstName LIMIT :start, :next", nativeQuery = true)
    public List<Object[]> findGoalsByDateBetween (@Param("advisorId") Long advisorId,
                                                  @Param("startDate") String startDate,
                                                  @Param("endDate") String endDate,
                                                  @Param("start") int start,
                                                  @Param("next") int next);

    @Query (value = "SELECT c.id clientId, c.client_first_name firstName, c.client_last_name lastName, g.type type, " +
            "COUNT(g.id) count FROM clients c " +
            "LEFT JOIN goals g ON g.client_id = c.id " +
            "WHERE c.advisor_id = :advisorId " +
            "AND g.creation_date <= ':endDate 23:59:59' " +
            "GROUP BY c.id, g.type, c.client_first_name " +
            "ORDER BY firstName LIMIT :start, :next", nativeQuery = true)
    public List<Object[]> findGoalsWithEndDate (@Param("advisorId") Long advisorId,
                                                @Param("endDate") String endDate,
                                                @Param("start") int start,
                                                @Param("next") int next);


    @Query (value = "SELECT c.id clientId, c.client_first_name firstName, c.client_last_name lastName, g.type type, " +
            "COUNT(g.id) count FROM clients c " +
            "LEFT JOIN goals g ON g.client_id = c.id " +
            "WHERE c.advisor_id = :advisorId " +
            "AND g.creation_date >= ':startDate 00:00:00' " +
            "GROUP BY c.id, g.type, c.client_first_name " +
            "ORDER BY firstName LIMIT :start, :next", nativeQuery = true)
    public List<Object[]> findGoalsWithStartDate (@Param("advisorId") Long advisorId,
                                                  @Param("startDate") String startDate,
                                                  @Param("start") int start,
                                                  @Param("next") int next);



}
