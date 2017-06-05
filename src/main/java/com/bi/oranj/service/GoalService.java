package com.bi.oranj.service;

import com.bi.oranj.entity.BiGoal;
import com.bi.oranj.json.GoalResponse;
import com.bi.oranj.repository.AdvisorRepository;
import com.bi.oranj.repository.ClientRepository;
import com.bi.oranj.repository.FirmRepository;
import com.bi.oranj.repository.GoalRepository;
import com.bi.oranj.wrapper.User;
import com.bi.oranj.wrapper.user.Firm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */
public interface GoalService {
    public Collection<? extends User> findGoals(int pageNum, long firmId, long advisorId);
    public int totalPages (long firmId, long advisorId);
    public GoalResponse buildResponse (int pageNum, long firmId, long advisorId, Collection<? extends User> users);
}
