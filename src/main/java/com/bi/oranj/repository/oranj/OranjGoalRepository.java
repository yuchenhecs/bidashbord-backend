package com.bi.oranj.repository.oranj;

import com.bi.oranj.model.oranj.OranjGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by harshavardhanpatil on 5/24/17.
 */
public interface OranjGoalRepository extends JpaRepository<OranjGoal, Integer> {

    List <OranjGoal> findByCreationDateBetween(String start, String end);
}
