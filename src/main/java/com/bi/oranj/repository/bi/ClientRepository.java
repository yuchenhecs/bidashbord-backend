package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by harshavardhanpatil on 6/2/17.
 */
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "SELECT COUNT(DISTINCT(id)) from clients WHERE advisor_id = ?1", nativeQuery = true)
    public Integer findDistinctByAdvisor(long advisorId);

    public Page<Client> findByAdvisorId(Long advisorId, Pageable pageable);

    @Query(value = "select count(distinct(c.id)) from clients c " +
            "left join goals g " +
            "on c.id = g.client_id " +
            "where c.advisor_id = :advisorId AND c.active = 1 AND" +
            "(g.goal_creation_date is null or g.goal_creation_date >= :startDate' 00:00:00')", nativeQuery = true)
    public Integer findDistinctClientsWithStartDate(@Param(value = "startDate") String startDate,
                                                     @Param(value = "advisorId") long advisorId);

    @Query(value = "select count(distinct(c.id)) from clients c " +
            "left join goals g " +
            "on c.id = g.client_id " +
            "where c.advisor_id = :advisorId AND c.active = 1 AND c.client_created_on <= :endDate' 23:59:59' AND " +
            "(g.goal_creation_date is null or g.goal_creation_date BETWEEN :startDate' 00:00:00' AND :endDate' 23:59:59')", nativeQuery = true)
    public Integer findDistinctClientsByDateBetween(@Param(value = "startDate") String startDate,
                                                     @Param(value = "endDate") String endDate,
                                                     @Param(value = "advisorId") long advisorId);

    @Query(value = "select count(distinct(c.id)) from clients c " +
            "left join goals g " +
            "on c.id = g.client_id " +
            "where c.advisor_id = :advisorId AND c.active = 1 AND c.client_created_on <= :endDate' 23:59:59' AND " +
            "(g.goal_creation_date is null or g.goal_creation_date <= :endDate' 23:59:59')", nativeQuery = true)
    public Integer findDistinctClientsWithEndDate(@Param(value = "endDate") String endDate,
                                                   @Param(value = "advisorId") long advisorId);


    @Query (value = "SELECT c.id clientId, c.client_first_name firstName, c.client_last_name lastName, g.type type, " +
            "COUNT(g.id) count FROM clients c " +
            "INNER JOIN ( " +
            "SELECT DISTINCT(c.id), c.client_first_name FROM clients c " +
            "left join goals g on " +
            "g.client_id = c.id " +
            "WHERE c.advisor_id = :advisorId AND c.active = 1 " +
            "ORDER BY c.client_first_name LIMIT :start, :next) t " +
            "on t.id = c.id " +
            "LEFT JOIN goals g ON g.client_id = c.id " +
            "GROUP BY c.id, c.client_first_name, g.type " +
            "ORDER BY firstName;", nativeQuery = true)
    public List<Object[]> findGoalsOrdered (@Param("advisorId") Long advisorId,
                                            @Param("start") int start,
                                            @Param("next") int next);


    @Query (value = "SELECT c.id clientId, c.client_first_name firstName, c.client_last_name lastName, g.type type, " +
            "COUNT(g.id) count FROM clients c " +
            "INNER JOIN ( " +
            "SELECT DISTINCT(c.id), c.client_first_name FROM clients c " +
            "LEFT join goals g on " +
            "g.client_id = c.id " +
            "WHERE c.advisor_id = :advisorId AND c.active = 1 AND " +
            "(g.goal_creation_date is null or g.goal_creation_date BETWEEN :startDate' 00:00:00' and :endDate' 23:59:59') " +
            "ORDER BY c.client_first_name LIMIT :start, :next) t " +
            "on t.id = c.id " +
            "LEFT JOIN goals g ON g.client_id = c.id " +
            "WHERE g.goal_creation_date is null or g.goal_creation_date BETWEEN :startDate' 00:00:00' and :endDate' 23:59:59' " +
            "GROUP BY c.id, c.client_first_name, g.type " +
            "ORDER BY firstName;", nativeQuery = true)
    public List<Object[]> findGoalsByDateBetween (@Param("advisorId") Long advisorId,
                                                  @Param("startDate") String startDate,
                                                  @Param("endDate") String endDate,
                                                  @Param("start") int start,
                                                  @Param("next") int next);

    @Query (value = "SELECT c.id clientId, c.client_first_name firstName, c.client_last_name lastName, g.type type, " +
            "COUNT(g.id) count FROM clients c " +
            "INNER JOIN ( " +
            "SELECT DISTINCT(c.id), c.client_first_name FROM clients c " +
            "left join goals g on " +
            "g.client_id = c.id " +
            "WHERE c.advisor_id = :advisorId AND c.active = 1 AND " +
            "(g.goal_creation_date is null or g.goal_creation_date <= :endDate' 23:59:59') " +
            "ORDER BY c.client_first_name LIMIT :start, :next) t " +
            "on t.id = c.id " +
            "LEFT JOIN goals g ON g.client_id = c.id " +
            "WHERE g.goal_creation_date is null or g.goal_creation_date <= :endDate' 23:59:59' " +
            "GROUP BY c.id, c.client_first_name, g.type " +
            "ORDER BY firstName;", nativeQuery = true)
    public List<Object[]> findGoalsWithEndDate (@Param("advisorId") Long advisorId,
                                                @Param("endDate") String endDate,
                                                @Param("start") int start,
                                                @Param("next") int next);


    @Query (value = "SELECT c.id clientId, c.client_first_name firstName, c.client_last_name lastName, g.type type, " +
            "COUNT(g.id) count FROM clients c " +
            "INNER JOIN ( " +
            "SELECT DISTINCT(c.id), c.client_first_name FROM clients c " +
            "left join goals g on " +
            "g.client_id = c.id " +
            "WHERE c.advisor_id = :advisorId AND c.active = 1 AND " +
            "(g.goal_creation_date is null or g.goal_creation_date >= :startDate' 00:00:00') " +
            "ORDER BY c.client_first_name LIMIT :start, :next) t " +
            "on t.id = c.id " +
            "LEFT JOIN goals g ON g.client_id = c.id " +
            "WHERE g.goal_creation_date is null or g.goal_creation_date >= :startDate' 00:00:00'" +
            "GROUP BY c.id, c.client_first_name, g.type " +
            "ORDER BY firstName;", nativeQuery = true)
    public List<Object[]> findGoalsWithStartDate (@Param("advisorId") Long advisorId,
                                                  @Param("startDate") String startDate,
                                                  @Param("start") int start,
                                                  @Param("next") int next);



}
