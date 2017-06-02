package com.bi.oranj.controller.oranj;

import com.bi.oranj.constant.Constants;
import com.bi.oranj.service.oranj.OranjService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by harshavardhanpatil on 5/25/17.
 */
@Api(basePath = "/oranj", description = "Operations with Oranj DB", produces = "application/json")
@RestController
@RequestMapping(value = "/oranj", produces=MediaType.APPLICATION_JSON_VALUE)
public class OranjController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    OranjService oranjService;

    /**
     * This API takes date in the YYYY-MM-DD format. Example -> {HostName}/oranj/goals?date=2016-12-20
     * Fetches Goals, users, firm data from Oranj db and stores in bi_goal table of bi_db
     * @param date
     * @return
     */
    @ApiOperation(value = "Get Goals created for given date", notes = "date should be in 'yyyy-MM-dd' format")
    @RequestMapping(path="/goals", method = RequestMethod.GET)
    public @ResponseBody String getGoalsByDate(@RequestParam(value = "date", required = true) String date) {

        String startDate = date + Constants.SPACE + Constants.START_SECOND_OF_THE_DAY;
        String endDate = date + Constants.SPACE + Constants.LAST_SECOND_OF_THE_DAY;
        log.info("Saving {} goals", date);
        return oranjService.getGoals(startDate, endDate);

    }
}
