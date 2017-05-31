package com.bi.oranj.repository.bi;

import com.bi.oranj.model.bi.BiGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jaloliddinbakirov on 5/24/17.
 */
public interface BiGoalRepository extends JpaRepository<BiGoal, Integer> {

    BiGoal findByGoalId(Long id);
}
