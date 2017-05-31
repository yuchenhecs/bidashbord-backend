package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.BiGoal;
import com.bi.oranj.repository.bi.BiGoalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by harshavardhanpatil on 5/30/17.
 */
@Service
public class BiService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BiGoalRepository biGoalRepository;

    public Iterable<BiGoal> getGoals(){
        return biGoalRepository.findAll();
    }
}
