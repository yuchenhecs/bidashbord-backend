package com.bi.oranj.controller.bi;

import com.bi.oranj.service.bi.GoalsService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("bi/goals")
public class GoalController {

    @Autowired
    GoalsService goalsService;

    @ApiOperation(value = "Get Goals at Admin level", notes = "returns goals")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping (value = "/firms", method = RequestMethod.GET)
    public ResponseEntity<Object> getFirmGoals (@RequestParam (value = "page", required = false) Integer pageNum,
                                                @RequestParam (value = "startDate", required = false) String startDate,
                                                @RequestParam (value = "endDate", required = false) String endDate) throws IOException {
        return goalsService.getUserGoals("firms", null, pageNum, startDate, endDate);
    }

    @ApiOperation(value = "Get Goals for Firm", notes = "returns goals")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping (value = "/advisors", method = RequestMethod.GET)
    public ResponseEntity<Object> getAdvisorGoals (@RequestParam (value = "page", required = false) Integer pageNum,
                                                   @RequestParam (value = "firmId", required = false) Long firmId,
                                                   @RequestParam (value = "startDate", required = false) String startDate,
                                                   @RequestParam (value = "endDate", required = false) String endDate) throws IOException {
        return goalsService.getUserGoals("advisors", firmId, pageNum, startDate, endDate);
    }

    @ApiOperation(value = "Get Goals for Advisor", notes = "returns goals")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping (value = "/clients", method = RequestMethod.GET)
    public ResponseEntity<Object> getClientGoals (@RequestParam (value = "page", required = false) Integer pageNum,
                                                  @RequestParam (value = "advisorId", required = false) Long advisorId,
                                                  @RequestParam (value = "startDate", required = false) String startDate,
                                                  @RequestParam (value = "endDate", required = false) String endDate) throws IOException {
        return goalsService.getUserGoals("clients", advisorId, pageNum, startDate, endDate);
    }

    @ApiOperation(value = "Get All Goals grouped by type", notes = "returns all goals grouped by type")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping (method = RequestMethod.GET)
    public ResponseEntity<Object> getGoalsSummary () throws IOException {
        return goalsService.getGoalsSummary();
    }
}
