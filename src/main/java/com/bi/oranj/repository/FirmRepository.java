package com.bi.oranj.repository;

import com.bi.oranj.entity.BiGoal;
import com.bi.oranj.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by jaloliddinbakirov on 5/25/17.
 */
public interface FirmRepository extends JpaRepository<BiGoal, Integer> {

    @Query(value = "SELECT DISTINCT(firmId) FROM BiGoal")
    public List<BiGoal> findDistinct();

    @Query(value="SELECT firm_id, firm_name, GROUP_CONCAT(type separator ','), COUNT(type) AS c FROM bi_goal GROUP " +
            "BY firm_id, firm_name ORDER BY firm_name LIMIT ?1, ?2", nativeQuery = true)

    public List<Object[]> findGoalsOrdered (int start, int next);

}