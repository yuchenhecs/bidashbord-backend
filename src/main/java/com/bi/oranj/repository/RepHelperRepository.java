package com.bi.oranj.repository;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */

import com.bi.oranj.entity.BiGoal;
import com.bi.oranj.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RepHelperRepository extends JpaRepository<BiGoal, Integer>{

}
