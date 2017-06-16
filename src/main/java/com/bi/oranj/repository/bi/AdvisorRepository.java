package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.Advisor;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bi.oranj.model.bi.BiGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Created by harshavardhanpatil on 6/2/17.
 */
public interface AdvisorRepository extends JpaRepository<Advisor, Long> {


    @Query (value = "SELECT COUNT(DISTINCT(id)) FROM advisors WHERE firm_id = ?1", nativeQuery = true)
    public Integer findDistinctByFirm(long firmId);

    @Query(value = "select count(distinct(a.id)) from advisors a " +
            "left join goals g " +
            "on a.id = g.advisor_id " +
            "where a.firm_id = :firmId AND g.creation_date >= :startDate' 00:00:00'", nativeQuery = true)
    public Integer findDistinctAdvisorsWithStartDate(@Param(value = "startDate") String startDate,
                                                     @Param(value = "firmId") long firmId);

    @Query(value = "select count(distinct(a.id)) from advisors a " +
            "left join goals g " +
            "on a.id = g.advisor_id " +
            "where a.firm_id = :firmId AND g.creation_date BETWEEN :startDate' 00:00:00' AND :endDate' 23:59:59'", nativeQuery = true)
    public Integer findDistinctAdvisorsByDateBetween(@Param(value = "startDate") String startDate,
                                                  @Param(value = "endDate") String endDate,
                                                  @Param(value = "firmId") long firmId);

    @Query(value = "select count(distinct(a.id)) from advisors a " +
            "left join goals g " +
            "on a.id = g.advisor_id " +
            "where a.firm_id = :firmId AND g.creation_date <= :endDate' 23:59:59'", nativeQuery = true)
    public Integer findDistinctAdvisorsWithEndDate(@Param(value = "endDate") String endDate,
                                                @Param(value = "firmId") long firmId);

    @Query (value = "SELECT a.id advisorId, a.advisor_first_name firstName, a.advisor_last_name lastName, g.type type, " +
            "COUNT(g.id) count FROM advisors a " +
            "LEFT JOIN goals g ON g.advisor_id = a.id WHERE a.firm_id = :firmId GROUP BY g.type, a.id, a.advisor_first_name " +
            "ORDER BY firstName LIMIT :start, :next", nativeQuery = true)
    public List<Object[]> findGoalsOrdered (@Param("firmId") Long firmId,
                                            @Param("start") int start,
                                            @Param("next") int next);


    @Query (value = "SELECT a.id advisorId, a.advisor_first_name firstName, a.advisor_last_name lastName, g.type type, " +
            "COUNT(g.id) count FROM advisors a " +
            "LEFT JOIN goals g ON g.advisor_id = a.id WHERE a.firm_id = :firmId " +
            "AND g.creation_date BETWEEN :startDate' 00:00:00' and :endDate' 23:59:59' " +
            "GROUP BY g.type, a.id, a.advisor_first_name ORDER BY firstName LIMIT :start, :next", nativeQuery = true)
    public List<Object[]> findGoalsByDateBetween (@Param("firmId") Long firmId,
                                                  @Param("startDate") String startDate,
                                                  @Param("endDate") String endDate,
                                                  @Param("start") int start,
                                                  @Param("next") int next);

    @Query (value = "SELECT a.id advisorId, a.advisor_first_name firstName, a.advisor_last_name lastName, g.type type, " +
            "COUNT(g.id) count FROM advisors a " +
            "LEFT JOIN goals g ON g.advisor_id = a.id " +
            "WHERE a.firm_id = :firmId " +
            "AND g.creation_date <= :endDate' 23:59:59' " +
            "GROUP BY g.type, a.id, a.advisor_first_name " +
            "ORDER BY firstName LIMIT :start, :next", nativeQuery = true)
    public List<Object[]> findGoalsWithEndDate (@Param("firmId") Long firmId,
                                                @Param("endDate") String endDate,
                                                @Param("start") int start,
                                                @Param("next") int next);


    @Query (value = "SELECT a.id advisorId, a.advisor_first_name firstName, a.advisor_last_name lastName, g.type type, " +
            "COUNT(g.id) count FROM advisors a " +
            "LEFT JOIN goals g ON g.advisor_id = a.id " +
            "WHERE a.firm_id = :firmId " +
            "AND g.creation_date >= :startDate' 00:00:00' " +
            "GROUP BY g.type, a.id, a.advisor_first_name " +
            "ORDER BY firstName LIMIT :start, :next", nativeQuery = true)
    public List<Object[]> findGoalsWithStartDate (@Param("firmId") Long firmId,
                                                  @Param("startDate") String startDate,
                                                  @Param("start") int start,
                                                  @Param("next") int next);

}
