package com.bi.oranj.repository;

import com.bi.oranj.entity.BiGoal;
import com.bi.oranj.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import java.util.List;

/**
 * Created by jaloliddinbakirov on 5/25/17.
 */
public interface FirmRepository extends JpaRepository<BiGoal, Integer> {

    @Query(value = "SELECT DISTINCT(firmId) FROM BiGoal")
    public List<BiGoal> findDistinct();


    @Query(value = "SELECT f.id firmId, f.firm_name firmName, GROUP_CONCAT(g.type separator ',') type, COUNT(g.goal_id) count FROM goals g \n" +
            "JOIN firms f ON g.firm_id = f.id  GROUP BY f.id, f.firm_name ORDER BY firmName LIMIT :start, :next", nativeQuery = true)
    public List<Object[]> findGoalsOrdered (@Param("start") int start, @Param("next") int next);

}