package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.Advisor;
import com.bi.oranj.model.bi.Firm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FirmRepository extends JpaRepository<Firm, Integer> {

    @Query(value = "SELECT COUNT(DISTINCT(id)) FROM firms", nativeQuery = true)
    public Integer findDistinctFromFirm();

    @Query(value = "select count(distinct(f.id)) from firms f " +
                    "left join goals g " +
                    "on f.id = g.firm_id " +
                    "where g.goal_creation_date >= :startDate' 00:00:00'", nativeQuery = true)
    public Integer findDistinctFirmsWithStartDate(@Param(value = "startDate") String startDate);

    @Query(value = "select count(distinct(f.id)) from firms f " +
            "left join goals g " +
            "on f.id = g.firm_id " +
            "where g.goal_creation_date BETWEEN :startDate' 00:00:00' AND :endDate' 23:59:59'", nativeQuery = true)
    public Integer findDistinctFirmsByDateBetween(@Param(value = "startDate") String startDate,
                                                  @Param(value = "endDate") String endDate);

    @Query(value = "select count(distinct(f.id)) from firms f " +
            "left join goals g " +
            "on f.id = g.firm_id " +
            "where g.goal_creation_date <= :endDate' 23:59:59'", nativeQuery = true)
    public Integer findDistinctFirmsWithEndDate(@Param(value = "endDate") String endDate);

    @Query (value = "select f.id firmId, f.firm_name firmName, g.type, count(g.id) " +
            "from firms f " +
            "left join goals g on g.firm_id = f.id " +
            "INNER JOIN (SELECT DISTINCT firm_name FROM firms ORDER BY firm_name LIMIT :start, :next) a " +
            "on a.firm_name = f.firm_name " +
            "GROUP BY f.id, f.firm_name, g.type " +
            "ORDER BY f.firm_name", nativeQuery = true)
    public List<Object[]> findGoalsOrdered (@Param("start") int start, @Param("next") int next);


    @Query (value = "select f.id firmId, f.firm_name firmName, g.type, count(g.id) \n" +
            "from firms f\n" +
            "left join goals g\n" +
            "on g.firm_id = f.id \n" +
            "INNER JOIN ( " +
            "SELECT DISTINCT(firm_name) FROM firms f " +
            "inner join goals g on " +
            "g.firm_id = f.id " +
            "WHERE g.goal_creation_date BETWEEN :startDate' 00:00:00' and :endDate' 23:59:59' " +
            "ORDER BY f.firm_name LIMIT :start, :next) a " +
            "on a.firm_name = f.firm_name " +
            "WHERE g.goal_creation_date BETWEEN :startDate' 00:00:00' and :endDate' 23:59:59' " +
            "GROUP BY f.id, f.firm_name, g.type " +
            "ORDER BY firmName", nativeQuery = true)
    public List<Object[]> findGoalsByDateBetween (@Param("startDate") String startDate,
                                                  @Param("endDate") String endDate,
                                                  @Param("start") int start,
                                                  @Param("next") int next);

    @Query (value = "select f.id firmId, f.firm_name firmName, g.type, count(g.id) \n" +
            "from firms f\n" +
            "left join goals g\n" +
            "on g.firm_id = f.id \n" +
            "INNER JOIN ( " +
            "SELECT DISTINCT(firm_name) FROM firms f " +
            "inner join goals g on " +
            "g.firm_id = f.id " +
            "WHERE g.goal_creation_date <= :endDate' 23:59:59' " +
            "ORDER BY f.firm_name LIMIT :start, :next) a " +
            "on a.firm_name = f.firm_name " +
            "WHERE g.goal_creation_date <= :endDate' 23:59:59' " +
            "GROUP BY f.id, f.firm_name, g.type " +
            "ORDER BY firmName", nativeQuery = true)
    public List<Object[]> findGoalsWithEndDate (@Param("endDate") String endDate,
                                                @Param("start") int start,
                                                @Param("next") int next);


    @Query (value = "select f.id firmId, f.firm_name firmName, g.type, count(g.id) \n" +
            "from firms f\n" +
            "left join goals g\n" +
            "on g.firm_id = f.id \n" +
            "INNER JOIN ( " +
            "SELECT DISTINCT(firm_name) FROM firms f " +
            "inner join goals g on " +
            "g.firm_id = f.id " +
            "WHERE g.goal_creation_date >= :startDate ' 00:00:00' " +
            "ORDER BY f.firm_name LIMIT :start, :next) a " +
            "on a.firm_name = f.firm_name " +
            "WHERE g.goal_creation_date >= :startDate ' 00:00:00' " +
            "GROUP BY f.id, f.firm_name, g.type " +
            "ORDER BY firmName", nativeQuery = true)
    public List<Object[]> findGoalsWithStartDate (@Param(value = "startDate") String startDate,
                                                  @Param("start") int start,
                                                  @Param("next") int next);

    public Page<Firm> findByActiveTrue(Pageable pageable);
}