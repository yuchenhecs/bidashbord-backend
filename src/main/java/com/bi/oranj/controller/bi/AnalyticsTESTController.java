package com.bi.oranj.controller.bi;

import com.bi.oranj.service.google.analytics.BiAnalyticsService;
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
public class AnalyticsTESTController {

    @Autowired
    BiAnalyticsService biAnalyticsService;

    @RequestMapping(path = "/initialize", method = RequestMethod.POST)
    public String fillInitialAnalyticsData (){
        return biAnalyticsService.getThirtyDaysAnalyticsData();
    }

    @RequestMapping(path = "/one-day-data", method = RequestMethod.POST)
    public String getDailyAnalytics (){
        return "";
    }

}
