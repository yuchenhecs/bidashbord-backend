package com.bi.oranj.service.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.Goal;
import com.bi.oranj.model.bi.GoalSummary;
import com.bi.oranj.repository.bi.GoalRepository;
import com.bi.oranj.utils.date.DateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoalsService {

    @Autowired
    FirmService firmService;

    @Autowired
    AdvisorService advisorService;

    @Autowired
    ClientService clientService;

    @Autowired
    DateValidator dateValidator;


    private final Logger log = LoggerFactory.getLogger(this.getClass());

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
            log.error("Error in fetching goals" + e);
            return RestResponse.error("Error in fetching Goals");
        }
        return RestResponse.successWithoutMessage(goalSummaryList);
    }

    public RestResponse getUserGoals (String userType, Long userId, Integer pageNum, String startDate, String endDate) throws IOException {
        GoalService goalService = getService(userType);

        if (userId == null) userId = Long.valueOf(0);
        if (pageNum == null) pageNum = Integer.valueOf(0);

        if (goalService == null || pageNum < 0){
            return RestResponse.error("Bad input parameter");
        }

        Goal goal = null;
        if (startDate == null && endDate == null)
            goal = goalService.buildResponse(pageNum, userId);
        else if (startDate == null && dateValidator.validate(endDate))
            goal = goalService.buildResponseWithEndDate(endDate, pageNum, userId);
        else if (endDate == null && dateValidator.validate(startDate))
            goal = goalService.buildResponseWithStartDate(startDate, pageNum, userId);
        else if (startDate != null && startDate != null && dateValidator.validate(startDate)
                && dateValidator.validate(endDate) && dateValidator.isLess(startDate, endDate))
            goal = goalService.buildResponseByDateBetween(startDate, endDate, pageNum, userId);
        else {
            return RestResponse.error("Bad input parameter");
        }

        if (goal == null) {
            return RestResponse.error("Data not found");
        }


        return RestResponse.successWithoutMessage(goal);
    }


    /**
     * returns GoalService based on userType
     *
     *
     * @param userType
     * @return
     */
    private GoalService getService (String userType){
        switch (userType.toLowerCase()){
            case "firms":
                return firmService;
            case "advisors":
                return advisorService;
            case "clients":
                return clientService;
            default:
                return null;
        }
    }
}
