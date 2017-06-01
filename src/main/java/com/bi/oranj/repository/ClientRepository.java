package com.bi.oranj.repository;

import com.bi.oranj.entity.BiGoal;
import com.bi.oranj.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */

public interface ClientRepository extends JpaRepository<BiGoal, Integer> {

}
