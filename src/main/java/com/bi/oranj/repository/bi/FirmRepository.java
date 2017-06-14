package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.Firm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by harshavardhanpatil on 6/2/17.
 */
public interface FirmRepository extends JpaRepository<Firm, Integer> {

    @Query(value = "SELECT DISTINCT(firmId) FROM BiGoal")
    public List<Firm> findDistinct();

    @Query(value = "SELECT DISTINCT(id) FROM firms", nativeQuery = true)
    public List<Object[]> findDistinctFromFirm();

//    @Query(value = "SELECT f.id firmId, f.firm_name firmName, g.type type, " +
//            "COUNT(g.id) count FROM goals g \n" +
//            "JOIN firms f ON g.firm_id = f.id  GROUP BY f.id, f.firm_name, g.type ORDER BY firmName LIMIT :start, :next", nativeQuery = true)
//    public List<Object[]> findGoalsOrdered (@Param("start") int start, @Param("next") int next);

    @Query (value = "select f.id firmId, f.firm_name firmName, g.type, count(g.id) " +
            "from firms f " +
            "left join goals g " +
            "on g.firm_id = f.id GROUP BY f.id, f.firm_name, g.type ORDER BY firmName LIMIT :start, :next", nativeQuery = true)
    public List<Object[]> findGoalsOrdered (@Param("start") int start, @Param("next") int next);
}