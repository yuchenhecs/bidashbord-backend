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


    @ApiOperation(value = "Get Goals for Users", notes = "returns goals")
    @RequestMapping (value = "/{userType}", method = RequestMethod.GET)
    public RestResponse getUserGoals (@RequestParam (value = "page", required = false) Integer pageNum,
                                                @RequestParam (value = "userId", required = true) Long userId,
                                                @RequestParam (value = "startDate", required = false) String startDate,
                                                @RequestParam (value = "endDate", required = false) String endDate,
                                                @PathVariable String userType) throws IOException {
        return goalsService.getUserGoals(userType, userId, pageNum, startDate, endDate);
    }

    @ApiOperation(value = "Get All Goals grouped by type", notes = "returns all goals grouped by type")
    @RequestMapping (method = RequestMethod.GET)
    public RestResponse getGoalsSummary () throws IOException {
        return goalsService.getGoalsSummary();
    }
}
