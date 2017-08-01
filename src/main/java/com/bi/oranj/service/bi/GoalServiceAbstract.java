package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.Goal;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */
public abstract class GoalServiceAbstract {

    public Goal buildResponseByDateBetween(String startDate, String endDate, int pageNum, long userId, HttpServletResponse response){
        return null;
    }

    public Goal buildResponseWithStartDate (String startdate, int pageNum, long userId, HttpServletResponse response){
        return null;
    }

    public Goal buildResponseWithEndDate (String endDate, int pageNum, long userId, HttpServletResponse response){
        return null;
    }

    public Goal buildResponse (int pageNum, long userId, HttpServletResponse response){
        return null;
    }
}
