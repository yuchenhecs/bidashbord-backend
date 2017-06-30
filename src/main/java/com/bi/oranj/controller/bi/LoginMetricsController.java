package com.bi.oranj.controller.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.service.bi.LoginMetricsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/bi/stats")
public class LoginMetricsController {

    @Autowired
    LoginMetricsService loginMetricsService;

    @ApiOperation(value = "Get Login Metrics for Oranj Admin", notes = "returns Login Metrics for Oranj Admin")
    @RequestMapping(path="/firms", method = RequestMethod.GET)
    public RestResponse getLoginMetricsForAdmin(@RequestParam(value = "page", required = true) Integer pageNumber,
                                                @RequestParam(value = "user", required = true) String user,
                                                @RequestParam(value = "range", required = true) String range) {
        return loginMetricsService.getLoginMetricsForAdmin(pageNumber, user, range);
    }

    @ApiOperation(value = "Get Login Metrics for Firm", notes = "returns Login Metrics for Firm Admin")
    @RequestMapping(path="/advisors", method = RequestMethod.GET)
    public RestResponse getLoginMetricsForFirm(@RequestParam (value = "firmId", required = true) Long firmId,
                                               @RequestParam(value = "page", required = true) Integer pageNumber,
                                               @RequestParam(value = "user", required = true) String user,
                                               @RequestParam(value = "range", required = true) String range) {
        return loginMetricsService.getLoginMetricsForFirm(firmId, pageNumber, user, range);
    }

}
