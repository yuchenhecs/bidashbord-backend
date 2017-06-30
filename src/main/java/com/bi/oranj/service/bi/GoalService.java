package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.GoalResponse;
import com.bi.oranj.model.bi.wrapper.User;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */
public abstract class GoalService {

    public GoalResponse buildResponseByDateBetween(String startDate, String endDate, int pageNum, long userId, HttpServletResponse response){
        return null;
    }

    public GoalResponse buildResponseWithStartDate (String startdate, int pageNum, long userId, HttpServletResponse response){
        return null;
    }

    public GoalResponse buildResponseWithEndDate (String endDate, int pageNum, long userId, HttpServletResponse response){
        return null;
    }

    public GoalResponse buildResponse (int pageNum, long userId, HttpServletResponse response){
        return null;
    }
}
