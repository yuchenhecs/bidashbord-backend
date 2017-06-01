package com.bi.oranj.service;

import com.bi.oranj.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */
@Service
public class GoalService {
    @Autowired
    private GoalRepository goalRepository;

}
