package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.GoalResponse;
import com.bi.oranj.model.bi.wrapper.User;

import java.util.Collection;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */
public interface GoalService {
    public Collection<? extends User> findGoals(int pageNum, long firmId, long advisorId);
    public int totalPages (long firmId, long advisorId);
    public GoalResponse buildResponse (int pageNum, long firmId, long advisorId, Collection<? extends User> users);
}
