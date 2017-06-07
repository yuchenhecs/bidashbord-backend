package com.bi.oranj.service.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.GoalSummary;
import com.bi.oranj.repository.bi.GoalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by harshavardhanpatil on 6/6/17.
 */
@Service
public class GoalsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    HttpServletResponse response;

    @Autowired
    private GoalRepository goalRepository;

    public RestResponse getGoalsSummary() {

        List<GoalSummary> goalSummaryList = new ArrayList<>();
        try {
            List<Object[]> goalsGroupedByType = goalRepository.findGoalsGroupedByType();
            for (Object[] goals : goalsGroupedByType) {
                GoalSummary goalSummary = new GoalSummary(goals[0].toString(), Long.parseLong(goals[1].toString()));
                log.info(goalSummary.getType() + ":" + goalSummary.getCount());
                goalSummaryList.add(goalSummary);
            }
        }catch (Exception e){
            log.error("Error in fecthing goals" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fecthing Goals from Oranj DB");
        }
        return RestResponse.successWithoutMessage(goalSummaryList);
    }
}
