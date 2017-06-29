package com.bi.oranj.controller.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.GoalResponse;
import com.bi.oranj.service.bi.AdvisorService;
import com.bi.oranj.service.bi.ClientService;
import com.bi.oranj.service.bi.FirmService;
import com.bi.oranj.service.bi.GoalService;
import com.bi.oranj.utils.date.DateValidator;
import com.bi.oranj.service.bi.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(basePath = "/bi/goals", description = "Operations with BI DB", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("bi/goals")
public class GoalController {

    private final Logger logger = LoggerFactory.getLogger(GoalController.class);

    @Autowired
    FirmService firmService;

    @Autowired
    AdvisorService advisorService;

    @Autowired
    ClientService clientService;

    @Autowired
    GoalsService goalsService;

    @Autowired
    DateValidator dateValidator;

    @ApiOperation(value = "Get Goals at Admin level", notes = "returns goals")
    @RequestMapping (value = "/firms", method = RequestMethod.GET)
    public RestResponse getFirmGoals (@RequestParam (value = "page", required = false) Integer pageNum, HttpServletRequest request,
                                HttpServletResponse response, @RequestParam (value = "startDate", required = false) String startDate,
                                    @RequestParam (value = "endDate", required = false) String endDate) throws IOException {
        return processRequest("firms", null, pageNum, response, startDate, endDate);
    }

    @ApiOperation(value = "Get Goals for Firm", notes = "returns goals")
    @RequestMapping (value = "/advisors", method = RequestMethod.GET)
    public RestResponse getAdvisorGoals (@RequestParam (value = "page", required = false) Integer pageNum, HttpServletRequest request,
                                HttpServletResponse response, @RequestParam (value = "firmId", required = true) Long firmId,
                                       @RequestParam (value = "startDate", required = false) String startDate,
                                       @RequestParam (value = "endDate", required = false) String endDate) throws IOException {
        return processRequest("advisors", firmId, pageNum, response, startDate, endDate);
    }

    @ApiOperation(value = "Get Goals for Advisor", notes = "returns goals")
    @RequestMapping (value = "/clients", method = RequestMethod.GET)
    public RestResponse getClientGoals (@RequestParam (value = "page", required = false) Integer pageNum, HttpServletRequest request,
                                HttpServletResponse response, @RequestParam (value = "advisorId", required = true) Long advisorId,
                                      @RequestParam (value = "startDate", required = false) String startDate,
                                      @RequestParam (value = "endDate", required = false) String endDate) throws IOException {
        return processRequest("clients", advisorId, pageNum, response, startDate, endDate);
    }


    private RestResponse processRequest (String userType, Long userId, Integer pageNum,
                                       HttpServletResponse response, String startDate, String endDate) throws IOException {
        GoalService goalService = getService(userType);

        if (userId == null) userId = Long.valueOf(0);
        if (pageNum == null) pageNum = Integer.valueOf(0);

        if (goalService == null || pageNum < 0){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Bad input parameter");
            return RestResponse.error("Bad input parameter");
        }

        RestResponse goalResponse = null;
        if (startDate == null && endDate == null)
            goalResponse = requestDefault(goalService, pageNum, userId, response);
        else if (startDate == null && dateValidator.validate(endDate))
            goalResponse = requestWithEndDate(goalService, pageNum, userId, response, endDate);
        else if (endDate == null && dateValidator.validate(startDate))
            goalResponse = requestWithStartDate(goalService, pageNum, userId, response, startDate);
        else if (startDate != null && startDate != null && dateValidator.validate(startDate)
                && dateValidator.validate(endDate) && dateValidator.isLess(startDate, endDate))
            goalResponse = requestByDateBetween(goalService, pageNum, userId, response, startDate, endDate);
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Bad input parameter");
        }

        if (goalResponse == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return RestResponse.error("Data not found");
        }
        return goalResponse;
    }


    private RestResponse requestByDateBetween(GoalService goalService, int pageNum, long userId,
                                         HttpServletResponse response, String startDate, String endDate) throws IOException {
        GoalResponse goals;
        try{
            goals = goalService.buildResponseByDateBetween(startDate, endDate, pageNum, userId, response);
        }catch (Exception ex){
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while building response for firms: " + ex);
            return null;
        }
        return RestResponse.successWithoutMessage(goals);
    }

    private RestResponse requestWithEndDate (GoalService goalService, int pageNum, long userId,
                                             HttpServletResponse response, String endDate) throws IOException {
        GoalResponse goals;
        try{
            goals = goalService.buildResponseWithEndDate(endDate, pageNum, userId, response);
        }catch (Exception ex){
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while building response for firms: " + ex);
            return null;
        }
        return RestResponse.successWithoutMessage(goals);
    }

    private RestResponse requestWithStartDate (GoalService goalService, int pageNum, long userId,
                                             HttpServletResponse response, String startDate) throws IOException {
        GoalResponse goals;
        try{
            goals = goalService.buildResponseWithStartDate(startDate, pageNum, userId, response);
        }catch (Exception ex){
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while building response for firms: " + ex);
            return null;
        }
        return RestResponse.successWithoutMessage(goals);
    }


    private RestResponse requestDefault (GoalService goalService, int pageNum, long userId,
                                         HttpServletResponse response) throws IOException {
        GoalResponse goals;
        try{
            goals = goalService.buildResponse(pageNum, userId, response);
        }catch (Exception ex){
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while building response for firms: " + ex);
            return null;
        }
        return RestResponse.successWithoutMessage(goals);
    }

    /**
     * returns GoalService based on userType
     *
     * NOTE: API considers firms, advisors, clients as users
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
        }
        return null;
    }

    @ApiOperation(value = "Get All Goals grouped by type", notes = "returns all goals grouped by type")
    @RequestMapping (method = RequestMethod.GET)
    public RestResponse getGoalsSummary () throws IOException {
        return goalsService.getGoalsSummary();
    }
}
