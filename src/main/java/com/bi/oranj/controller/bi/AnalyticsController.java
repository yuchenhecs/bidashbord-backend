package com.bi.oranj.controller.bi;

import com.bi.oranj.service.google.analytics.BiAnalyticsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jaloliddinbakirov on 6/28/17.
 */
@RestController
@CrossOrigin
@RequestMapping("bi/analytics")
public class AnalyticsController {

    @Autowired
    BiAnalyticsService biAnalyticsService;

    @ApiOperation(value = "Get Prospect and Client data", notes = "returns 30 days data from Google Analytics")
    @RequestMapping(path = "/migration", method = RequestMethod.GET)
    public String fillInitialAnalyticsData (){
        biAnalyticsService.getPreciseOneMonthAnalyticsDayBy();
        return "Success";
    }

    @ApiOperation(value = "Get Prospect and Client data For One day", notes = "returns yesterdays data from Google Analytics")
    @RequestMapping(path = "/one-day-data", method = RequestMethod.GET)
    public String getDailyAnalytics (){
        return biAnalyticsService.getAnalyticsDataForYesterday();
    }

}
