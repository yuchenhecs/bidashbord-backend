package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.GoalSummary;
import com.bi.oranj.repository.bi.GoalRepository;
import com.bi.oranj.utils.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoalsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    HttpServletResponse response;

    @Autowired
    private GoalRepository goalRepository;

    public ResponseEntity<Object> getGoalsSummary() {

        List<GoalSummary> goalSummaryList = new ArrayList<>();
        try {
            List<Object[]> goalsGroupedByType = goalRepository.findGoalsGroupedByType();
            for (Object[] goals : goalsGroupedByType) {
                GoalSummary goalSummary = new GoalSummary(goals[0].toString(), Long.parseLong(goals[1].toString()));
                log.info(goalSummary.getType() + ":" + goalSummary.getCount());
                goalSummaryList.add(goalSummary);
            }
        }catch (Exception e){
            log.error("Error in fetching goals", e);
            return new ResponseEntity<>(new ApiError("Error in fetching Goals"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(goalSummaryList, HttpStatus.OK);
    }
}
