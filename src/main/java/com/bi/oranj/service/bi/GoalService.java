package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.GoalResponse;
import com.bi.oranj.model.bi.wrapper.User;

import java.util.Collection;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */
public abstract class GoalService {
    public int totalPages (long userId){
        return 0;
    }

    public GoalResponse buildResponseWithDate (String startDate, String endDate, int pageNum, long userId){
        return null;
    }

    public GoalResponse buildResponseWithStartDate (String startdate, int pageNum, long userId){
        return null;
    }

    public GoalResponse buildResponseWithEndDate (String endDate, int pageNum, long userId){
        return null;
    }

    public GoalResponse buildResponse (int pageNum, long userId){
        return null;
    }
}
