package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.Goal;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */
public abstract class GoalService {
    public int totalPages (long userId){
        return 0;
    }

    public int totalPagesWithStartDate (long userId, String startDate) {return 0;}

    public int totalPagesWithEndDate (long userId, String endDate) {return 0;}

    public int totalPagesByDateBetween (long userId, String startDate, String endDate) {return 0;}

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
