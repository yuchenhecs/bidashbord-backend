package com.bi.oranj.controller.bi;

import com.bi.oranj.service.bi.GamificationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/bi/gamification", produces= MediaType.APPLICATION_JSON_VALUE)
public class GamificationController {

    @Autowired
    private GamificationService gamificationService;

    @ApiOperation(value = "Get advisor summary", notes = "'advisorId' should be an integer value")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping(value="/advisors/summary", method = RequestMethod.GET)
    public ResponseEntity<Object> getAdvisorSummaryForGamification() {
        return gamificationService.getAdvisorSummaryForGamification();
    }

    @ApiOperation(value = "Get advisor achievements", notes = "'advisorId' should be an integer value")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping(value="/advisors/patOnTheBack", method = RequestMethod.GET)
    public ResponseEntity<Object> getPatOnTheBackMessage(@RequestParam (value = "region", required = true) String region) {
        return gamificationService.getAdvisorAchievements(region);
    }
}