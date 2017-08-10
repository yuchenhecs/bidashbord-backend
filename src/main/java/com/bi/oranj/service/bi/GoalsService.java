package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.Client;
import com.bi.oranj.model.bi.Goal;
import com.bi.oranj.model.bi.GoalSummary;
import com.bi.oranj.repository.bi.ClientRepository;
import com.bi.oranj.repository.bi.GoalRepository;
import com.bi.oranj.utils.date.DateValidator;
import com.bi.oranj.utils.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoalsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FirmService firmService;

    @Autowired
    AdvisorService advisorService;

    @Autowired
    ClientService clientService;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    GoalRepository goalRepository;

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    DateValidator dateValidator;

    public ResponseEntity<Object> getGoalsSummary() {

        List<Object[]> goalsGroupedByType = null;

        if (authorizationService.isSuperAdmin()){
            goalsGroupedByType = goalRepository.findGoalsGroupedByType();
        } else if (authorizationService.isAdmin()){
            Client client = clientRepository.findById(authorizationService.getUserId());
            if (client == null) return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
            goalsGroupedByType = goalRepository.findGoalsGroupedByTypeForFirm(client.getFirmId());
        } else if (authorizationService.isAdvisor()){
            Client client = clientRepository.findById(authorizationService.getUserId());
            if (client == null) return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
            goalsGroupedByType = goalRepository.findGoalsGroupedByTypeForAdvisor(client.getAdvisorId());
        } else {
            return new ResponseEntity<Object>("FORBIDDEN", HttpStatus.FORBIDDEN);
        }

        List<GoalSummary> goalSummaryList = new ArrayList<>();
        try {
            for (Object[] goals : goalsGroupedByType) {
                GoalSummary goalSummary = new GoalSummary(goals[0].toString(), Long.parseLong(goals[1].toString()));
                goalSummaryList.add(goalSummary);
            }
        }catch (Exception e){
            log.error("Error in fetching goals", e);
            return new ResponseEntity<>(new ApiResponseMessage("Error in fetching Goals"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(goalSummaryList, HttpStatus.OK);
    }


    public ResponseEntity<Object> getUserGoals (String userType, Long userId, Integer pageNum,
                                                   String startDate, String endDate) throws IOException {
        GoalServiceAbstract goalServiceAbstract = getService(userType);

        if (userId == null) {
            if (authorizationService.isSuperAdmin()) {
                userId = Long.valueOf(0);
            } else if (authorizationService.isAdmin()) {
                Client client = clientRepository.findById(authorizationService.getUserId());
                userId = client.getFirmId();
            } else if (authorizationService.isAdvisor()) {
                Client client = clientRepository.findById(authorizationService.getUserId());
                userId = client.getAdvisorId();
            }
        }

        if (pageNum == null) pageNum = Integer.valueOf(0);

        if (goalServiceAbstract == null || pageNum < 0) {
            return new ResponseEntity<>(new ApiResponseMessage("Bad input parameter"), HttpStatus.BAD_REQUEST);
        }

        Goal goal = null;
        if (startDate == null && endDate == null)
            goal = goalServiceAbstract.buildResponse(pageNum, userId);
        else if (startDate == null && dateValidator.validate(endDate))
            goal = goalServiceAbstract.buildResponseWithEndDate(endDate, pageNum, userId);
        else if (endDate == null && dateValidator.validate(startDate))
            goal = goalServiceAbstract.buildResponseWithStartDate(startDate, pageNum, userId);
        else if (startDate != null && startDate != null && dateValidator.validate(startDate)
                && dateValidator.validate(endDate) && dateValidator.isLessOrEqual(startDate, endDate))
            goal = goalServiceAbstract.buildResponseByDateBetween(startDate, endDate, pageNum, userId);
        else {
            return new ResponseEntity<>(new ApiResponseMessage("Bad input parameter"), HttpStatus.BAD_REQUEST);
        }

        if (goal == null) {
            return new ResponseEntity<>(new Goal(), HttpStatus.OK);
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
