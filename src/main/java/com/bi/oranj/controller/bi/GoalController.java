package com.bi.oranj.controller.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.Goal;
import com.bi.oranj.service.bi.AdvisorService;
import com.bi.oranj.service.bi.ClientService;
import com.bi.oranj.service.bi.FirmService;
import com.bi.oranj.service.bi.GoalService;
import com.bi.oranj.utils.date.DateValidator;
import com.bi.oranj.service.bi.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("bi/goals")
public class GoalController {



    @Autowired
    GoalsService goalsService;


    @ApiOperation(value = "Get Goals for Super Admin", notes = "returns goals")
    @RequestMapping (value = "/firms", method = RequestMethod.GET)
    public RestResponse getSuperAdminGoals (@RequestParam (value = "page", required = false) Integer pageNum,
                                                @RequestParam (value = "startDate", required = false) String startDate,
                                                @RequestParam (value = "endDate", required = false) String endDate
                                                ) throws IOException {
        return goalsService.getUserGoals("firms", null, pageNum, startDate, endDate);
    }

    @ApiOperation(value = "Get Goals for Firms", notes = "returns goals")
    @RequestMapping (value = "/advisors", method = RequestMethod.GET)
    public RestResponse getFirmGoals (@RequestParam (value = "page", required = false) Integer pageNum,
                                      @RequestParam (value = "firmId", required = true) Long userId,
                                      @RequestParam (value = "startDate", required = false) String startDate,
                                      @RequestParam (value = "endDate", required = false) String endDate
                                      ) throws IOException {
        return goalsService.getUserGoals("advisors", userId, pageNum, startDate, endDate);
    }

    @ApiOperation(value = "Get Goals for Advisors", notes = "returns goals")
    @RequestMapping (value = "/clients", method = RequestMethod.GET)
    public RestResponse getAdvisorGoals (@RequestParam (value = "page", required = false) Integer pageNum,
                                      @RequestParam (value = "advisorId", required = true) Long userId,
                                      @RequestParam (value = "startDate", required = false) String startDate,
                                      @RequestParam (value = "endDate", required = false) String endDate) throws IOException {
        return goalsService.getUserGoals("clients", userId, pageNum, startDate, endDate);
    }

    @ApiOperation(value = "Get All Goals grouped by type", notes = "returns all goals grouped by type")
    @RequestMapping (method = RequestMethod.GET)
    public RestResponse getGoalsSummary () throws IOException {
        return goalsService.getGoalsSummary();
    }
}
