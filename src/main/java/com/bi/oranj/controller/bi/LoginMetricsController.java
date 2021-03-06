package com.bi.oranj.controller.bi;

import com.bi.oranj.service.bi.LoginMetricsService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/bi/stats")
public class LoginMetricsController {

    @Autowired
    LoginMetricsService loginMetricsService;

    @ApiOperation(value = "Get Login Metrics for Oranj Admin", notes = "returns Login Metrics for Oranj Admin")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping(path="/firms", method = RequestMethod.GET)
    public ResponseEntity<Object> getLoginMetricsForAdmin(@RequestParam(value = "page", required = true) Integer pageNumber,
                                                @RequestParam(value = "user", required = true) String user,
                                                @RequestParam(value = "range", required = true) String range) {
        return loginMetricsService.getLoginMetricsForAdmin(pageNumber, user, range);
    }

    @ApiOperation(value = "Get Login Metrics for Firm", notes = "returns Login Metrics for Firm Admin")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping(path="/advisors", method = RequestMethod.GET)
    public ResponseEntity<Object> getLoginMetricsForFirm(@RequestParam (value = "firmId", required = false) Long firmId,
                                               @RequestParam(value = "page", required = true) Integer pageNumber,
                                               @RequestParam(value = "user", required = true) String user,
                                               @RequestParam(value = "range", required = true) String range) {
        return loginMetricsService.getLoginMetricsForFirm(firmId, pageNumber, user, range);
    }

    @ApiOperation(value = "Get Login Metrics for Firm", notes = "returns Login Metrics for Firm Admin")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping(path="/clients", method = RequestMethod.GET)
    public ResponseEntity<Object> getLoginMetricsForAdvisor(@RequestParam (value = "advisorId", required = false) Long advisorId,
                                                  @RequestParam(value = "page", required = true) Integer pageNumber,
                                                  @RequestParam(value = "user", required = true) String user,
                                                  @RequestParam(value = "range", required = true) String range) {
        return loginMetricsService.getLoginMetricsForAdvisor(advisorId, pageNumber, user, range);
    }

    @ApiOperation(value = "Get Login Metrics for Firm", notes = "returns Login Metrics for Firm Admin")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> getLoginMetricsSummary(@RequestParam(value = "user", required = true) String user) {
        return loginMetricsService.getLoginMetricsSummary(user);
    }

}