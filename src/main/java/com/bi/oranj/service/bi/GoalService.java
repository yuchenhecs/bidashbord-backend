package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.GoalResponse;
import com.bi.oranj.model.bi.wrapper.User;

import java.util.Collection;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */
public interface GoalService {
    public Collection<? extends User> findGoals(int pageNum, long userId);
    public int totalPages (long userId);
    public GoalResponse buildResponse (int pageNum, long userId, Collection<? extends User> users);
}
