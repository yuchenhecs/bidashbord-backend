package com.bi.oranj.controller.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.service.bi.GamificationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/bi/gamification", produces= MediaType.APPLICATION_JSON_VALUE)
public class GamificationController {

    @Autowired
    private GamificationService gamificationService;

    @ApiOperation(value = "Get advisor summary", notes = "'advisorId' should be an integer value")
    @RequestMapping(value="/advisors/{advisorId}/summary", method = RequestMethod.GET)
    public RestResponse getAdvisorSummaryForGamification(@PathVariable("advisorId") Long advisorId) {
        return gamificationService.getAdvisorSummaryForGamification(advisorId);
    }

    @ApiOperation(value = "Get advisor Rank", notes = "'advisorId' should be an integer value")
    @RequestMapping(value="/advisors/{advisorId}/rank", method = RequestMethod.GET)
    public RestResponse getAdvisorRankForGamification(@PathVariable("advisorId") Long advisorId) {
        return gamificationService.getAdvisorRankForGamification(advisorId);
    }
}
