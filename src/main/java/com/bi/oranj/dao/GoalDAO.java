package com.bi.oranj.dao;

import com.bi.oranj.model.Goal;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by jaloliddinbakirov on 5/24/17.
 */
@Transactional
public interface GoalDAO extends CrudRepository<Goal, Long>{
    public Goal findByName(String name);
}
