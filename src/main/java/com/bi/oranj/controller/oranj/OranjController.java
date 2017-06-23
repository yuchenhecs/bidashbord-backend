package com.bi.oranj.controller.oranj;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.service.oranj.OranjService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;

@Api(basePath = "/oranj", description = "Operations with Oranj DB", produces = "application/json")
@RestController
@RequestMapping(value = "/bi/oranj", produces=MediaType.APPLICATION_JSON_VALUE)
public class OranjController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    OranjService oranjService;

    /**
     * This API takes date in the YYYY-MM-DD format. Example -> {HostName}/oranj/goals?date=2016-12-20
     * Fetches Goals, users, firm data from Oranj db and stores in bi_goal table of bi_db
     * @param date
     * @return
     */
    @ApiOperation(value = "Save Goals created from Oranj DB to BI DB for given date", notes = "date should be in 'yyyy-MM-dd' format")
    @RequestMapping(path="/goals", method = RequestMethod.GET)
    public RestResponse getGoalsByDate(@RequestParam(value = "date", required = true) String date) {
        log.info("Saving {} goals", date);
        return oranjService.getGoals(date);
    }

    @ApiOperation(value = "Save All Goals from Oranj DB To BI DB", notes = "Saves all the goals created till today")
    @RequestMapping(path="/goals/migration", method = RequestMethod.GET)
    public RestResponse getGoalsTillDate() {
        String date = today().toString();
        log.info("Fetching goals till {}", date);
        return oranjService.getGoalsTillDate(date);
    }

    public Date today() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        log.info(cal.getTime().toString());
        return cal.getTime();
    }


    @ApiOperation( value = "Build initial historical data")
    @RequestMapping (path = "/aum", method = RequestMethod.GET)
    public void buildData (HttpServletResponse response){
        log.info("Building initial data");
        try{
            oranjService.fetchPositionsHistory(50);
            oranjService.fetchPositionsData();
        }catch (Exception ex){
            log.error("Error while building initial data");
            ex.printStackTrace();
        }

    }

    @ApiOperation(value = "Save All Firms from Oranj DB To BI DB", notes = "Saves all the firms created till today")
    @RequestMapping(path="/firms", method = RequestMethod.GET)
    public RestResponse getAllFirms() {
        log.info("Fetching All Firms From Oranj DB");
        return oranjService.getAllFirms();
    }

    @ApiOperation(value = "Save All Advisors from Oranj DB To BI DB", notes = "Saves all the advisors created till today")
    @RequestMapping(path="/advisors", method = RequestMethod.GET)
    public RestResponse getAllAdvisors() {
        log.info("Fetching All Firms From Oranj DB");
        return oranjService.getAllAdvisors();
    }


    @ApiOperation(value = "Save All Clients from Oranj DB To BI DB", notes = "Saves all the clients created till today")
    @RequestMapping(path="/clients", method = RequestMethod.GET)
    public RestResponse getAllClients() {
        log.info("Fetching All Firms From Oranj DB");
        return oranjService.getAllClients();
    }


    @ApiOperation( value = "Save All Net Worth from Oranj DB To BI DB", notes = "Saves all the net worth till today")
    @RequestMapping (path = "/networth/migration", method = RequestMethod.GET)
    public RestResponse getNetworthTillDate (){
        String date = today().toString();
        log.info("Fetching All Net Worth From Oranj DB");
        return oranjService.getNetWorthTillDate(date);
    }

    @ApiOperation(value = "Save Net Worth from Oranj DB to BI DB for given date", notes = "date should be in 'yyyy-MM-dd' format")
    @RequestMapping (path = "/networth", method = RequestMethod.GET)
    public RestResponse getNetworth (@RequestParam(value = "date", required = true) String date){
        log.info("Fetching Net Worth From Oranj DB for "+ date);
        return oranjService.getNetWorth(date);
    }

}
