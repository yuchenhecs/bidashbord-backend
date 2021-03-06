package com.bi.oranj.controller.oranj;

import com.bi.oranj.service.oranj.OranjService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;

import static com.bi.oranj.constant.Constants.YEAR_MONTH_DAY_FORMAT;

@RestController
@RequestMapping(value = "/bi/oranj", produces=MediaType.APPLICATION_JSON_VALUE)
public class OranjController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(YEAR_MONTH_DAY_FORMAT);

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
    public ResponseEntity<Object> getGoalsByDate(@RequestParam(value = "date", required = true) String date) {
        log.info("Saving {} goals", date);
        return oranjService.getGoals(date);
    }

    @ApiOperation(value = "Save All Goals from Oranj DB To BI DB", notes = "Saves all the goals created till today")
    @RequestMapping(path="/goals/migration", method = RequestMethod.GET)
    public ResponseEntity<Object> getGoalsTillDate() {
        String date = dateFormat.format(today());
        log.info("Fetching goals till {}", date);
        return oranjService.getGoalsTillDate(date);
    }

    public Date today() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        log.info("Today's date {}",cal.getTime());
        return cal.getTime();
    }


    @ApiOperation( value = "Build initial historical data")
    @RequestMapping (path = "/aum", method = RequestMethod.GET)
    public ResponseEntity buildData (@RequestParam(value = "limit", required = false) Integer limit, HttpServletResponse response){
        log.info("Building initial data");
        try{
            if (limit == null) limit = 0;
            oranjService.fetchPositionsHistory(limit);
            oranjService.fetchPositionsData();
        }catch (Exception ex){
            log.error("Error while building initial data", ex);
            new ResponseEntity<>("Error while fetching positions data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Save All Firms from Oranj DB To BI DB", notes = "Saves all the firms created till today")
    @RequestMapping(path="/firms", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllFirms() {
        log.info("Fetching All Firms From Oranj DB");
        return oranjService.getAllFirms();
    }

    @ApiOperation(value = "Save All Advisors from Oranj DB To BI DB", notes = "Saves all the advisors created till today")
    @RequestMapping(path="/advisors", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllAdvisors() {
        log.info("Fetching All Advisors From Oranj DB");
        return oranjService.getAllAdvisors();
    }


    @ApiOperation(value = "Save All Clients from Oranj DB To BI DB", notes = "Saves all the clients created till today")
    @RequestMapping(path="/clients", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllClients() {
        log.info("Fetching All Clinets From Oranj DB");
        return oranjService.getAllClients();
    }


    @ApiOperation( value = "Save All Net Worth from Oranj DB To BI DB", notes = "Saves all the net worth till today")
    @RequestMapping (path = "/networth/migration", method = RequestMethod.GET)
    public ResponseEntity<Object> getNetworthTillDate (){
        String date = dateFormat.format(today());
        log.info("Fetching All Net Worth From Oranj DB");
        return oranjService.getNetWorthTillDate(date);
    }

    @ApiOperation(value = "Save Net Worth from Oranj DB to BI DB for given date", notes = "date should be in 'yyyy-MM-dd' format")
    @RequestMapping (path = "/networth", method = RequestMethod.GET)
    public ResponseEntity<Object> getNetworth (@RequestParam(value = "date", required = true) String date){
        log.info("Fetching Net Worth From Oranj DB for ", date);
        return oranjService.getNetWorthForDate(date);
    }
}
