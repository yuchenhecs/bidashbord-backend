package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.Goal;
import com.bi.oranj.model.bi.GoalSummary;
import com.bi.oranj.repository.bi.GoalRepository;
import com.bi.oranj.utils.ApiError;
import com.bi.oranj.utils.date.DateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoalsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    HttpServletResponse response;

    @Autowired
    private FirmServiceAbstract firmService;

    @Autowired
    private AdvisorServiceAbstract advisorService;

    @Autowired
    private ClientServiceAbstract clientService;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    DateValidator dateValidator;

    public ResponseEntity<Object> getGoalsSummary() {

        List<Object[]> goalsGroupedByType = null;

        if (authorizationService.isSuperAdmin()){
            goalsGroupedByType = goalRepository.findGoalsGroupedByType();
        } else if (authorizationService.isAdvisor()){
            goalsGroupedByType = goalRepository.findGoalsGroupedByTypeForAdvisor(authorizationService.getUserId());
        } else if (authorizationService.isAdmin()){
            goalsGroupedByType = goalRepository.findGoalsGroupedByTypeForFirm(authorizationService.getUserId());
        } else {
            return new ResponseEntity<Object>("FORBIDDEN", HttpStatus.FORBIDDEN);
        }

        List<GoalSummary> goalSummaryList = new ArrayList<>();
        try {
//            List<Object[]> goalsGroupedByType = goalRepository.findGoalsGroupedByType();
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


    public ResponseEntity<Object> getUserGoals (String userType, Long userId, Integer pageNum,
                                                   String startDate, String endDate) throws IOException {
        GoalServiceAbstract goalServiceAbstract = getService(userType);

        if (userId == null) userId = Long.valueOf(0);
        if (pageNum == null) pageNum = Integer.valueOf(0);

        if (goalServiceAbstract == null || pageNum < 0){
            return new ResponseEntity<>(new ApiError("Bad input parameter"), HttpStatus.BAD_REQUEST);
        }

        Goal goal = null;
        if (startDate == null && endDate == null)
            goal = goalServiceAbstract.buildResponse(pageNum, userId, response);
        else if (startDate == null && dateValidator.validate(endDate))
            goal = goalServiceAbstract.buildResponseWithEndDate(endDate, pageNum, userId, response);
        else if (endDate == null && dateValidator.validate(startDate))
            goal = goalServiceAbstract.buildResponseWithStartDate(startDate, pageNum, userId, response);
        else if (startDate != null && startDate != null && dateValidator.validate(startDate)
                && dateValidator.validate(endDate) && dateValidator.isLess(startDate, endDate))
            goal = goalServiceAbstract.buildResponseByDateBetween(startDate, endDate, pageNum, userId, response);
        else {
            return new ResponseEntity<>(new ApiError("Bad input parameter"), HttpStatus.BAD_REQUEST);
        }

        if (goal == null) {
            return new ResponseEntity<>(new ApiError("Data not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(goal, HttpStatus.OK);
    }


    private GoalServiceAbstract getService (String userType){
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
