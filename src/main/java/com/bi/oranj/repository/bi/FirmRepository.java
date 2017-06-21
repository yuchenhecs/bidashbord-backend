package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.Firm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FirmRepository extends JpaRepository<Firm, Integer> {

    @Query(value = "SELECT COUNT(DISTINCT(id)) FROM firms where active = 1", nativeQuery = true)
    public Integer findDistinctFromFirm();

    @Query(value = "select count(distinct(f.id)) from firms f " +
                    "left join goals g " +
                    "on f.id = g.firm_id " +
                    "where f.active = 1 and (g.goal_creation_date is null or g.goal_creation_date >= :startDate' 00:00:00')", nativeQuery = true)
    public Integer findDistinctFirmsWithStartDate(@Param(value = "startDate") String startDate);

    @Query(value = "select count(distinct(f.id)) from firms f " +
            "left join goals g " +
            "on f.id = g.firm_id " +
            "where f.active = 1 and f.firm_created_on <= :endDate' 23:59:59' and " +
            "(g.goal_creation_date is null or g.goal_creation_date BETWEEN :startDate' 00:00:00' AND :endDate' 23:59:59')", nativeQuery = true)
    public Integer findDistinctFirmsByDateBetween(@Param(value = "startDate") String startDate,
                                                  @Param(value = "endDate") String endDate);

    @Query(value = "select count(distinct(f.id)) from firms f " +
            "left join goals g " +
            "on f.id = g.firm_id " +
            "where f.active = 1 and f.firm_created_on <= :endDate' 23:59:59' and " +
            "(g.goal_creation_date is null or g.goal_creation_date <= :endDate' 23:59:59')", nativeQuery = true)
    public Integer findDistinctFirmsWithEndDate(@Param(value = "endDate") String endDate);

    @Query (value = "select f.id firmId, f.firm_name firmName, g.type, count(g.id) " +
            "from firms f " +
            "left join goals g on g.firm_id = f.id " +
            "INNER JOIN (SELECT DISTINCT firm_name FROM firms where active = 1 ORDER BY firm_name LIMIT :start, :next) a " +
            "on a.firm_name = f.firm_name " +
            "GROUP BY f.id, f.firm_name, g.type " +
            "ORDER BY f.firm_name", nativeQuery = true)
    public List<Object[]> findGoalsOrdered (@Param("start") int start, @Param("next") int next);


    @Query (value = "select f.id firmId, f.firm_name firmName, g.type, count(g.id) \n" +
            "from firms f\n" +
            "INNER JOIN ( " +
            "SELECT DISTINCT(f.id), f.firm_name FROM firms f " +
            "left join goals g on " +
            "g.firm_id = f.id " +
            "WHERE f.active = 1 and f.firm_created_on <= :endDate' 23:59:59' and (g.goal_creation_date is null or " +
            "g.goal_creation_date BETWEEN :startDate' 00:00:00' and :endDate' 23:59:59') " +
            "ORDER BY f.firm_name LIMIT :start, :next) a " +
            "on a.id = f.id " +
            "left join goals g " +
            "on g.firm_id = f.id " +
            "WHERE g.goal_creation_date is null or g.goal_creation_date BETWEEN :startDate' 00:00:00' and :endDate' 23:59:59' " +
            "GROUP BY f.id, f.firm_name, g.type " +
            "ORDER BY firmName", nativeQuery = true)
    public List<Object[]> findGoalsByDateBetween (@Param("startDate") String startDate,
                                                  @Param("endDate") String endDate,
                                                  @Param("start") int start,
                                                  @Param("next") int next);

    @Query (value = "select f.id firmId, f.firm_name firmName, g.type, count(g.id) \n" +
            "from firms f\n" +
            "INNER JOIN ( " +
            "SELECT DISTINCT(f.id), f.firm_name FROM firms f " +
            "left join goals g on " +
            "g.firm_id = f.id " +
            "WHERE active = 1 and f.firm_created_on <= :endDate' 23:59:59' and (g.goal_creation_date is null or " +
            "g.goal_creation_date <= :endDate' 23:59:59') " +
            "ORDER BY f.firm_name LIMIT :start, :next) a " +
            "on a.id = f.id " +
            "left join goals g " +
            "on g.firm_id = f.id " +
            "WHERE g.goal_creation_date is null or g.goal_creation_date <= :endDate' 23:59:59' " +
            "GROUP BY f.id, f.firm_name, g.type " +
            "ORDER BY firmName", nativeQuery = true)
    public List<Object[]> findGoalsWithEndDate (@Param("endDate") String endDate,
                                                @Param("start") int start,
                                                @Param("next") int next);


    @Query (value = "select f.id firmId, f.firm_name firmName, g.type, count(g.id) \n" +
            "from firms f\n" +
            "INNER JOIN ( " +
            "SELECT DISTINCT(f.id), f.firm_name FROM firms f " +
            "left join goals g on " +
            "g.firm_id = f.id " +
            "WHERE active = 1 and (g.goal_creation_date is null or g.goal_creation_date >= :startDate ' 00:00:00') " +
            "ORDER BY f.firm_name LIMIT :start, :next) a " +
            "on a.firm_name = f.firm_name " +
            "left join goals g\n" +
            "on g.firm_id = f.id \n" +
            "WHERE g.goal_creation_date is null or g.goal_creation_date >= :startDate ' 00:00:00' " +
            "GROUP BY f.id, f.firm_name, g.type " +
            "ORDER BY firmName", nativeQuery = true)
    public List<Object[]> findGoalsWithStartDate (@Param(value = "startDate") String startDate,
                                                  @Param("start") int start,
                                                  @Param("next") int next);
}