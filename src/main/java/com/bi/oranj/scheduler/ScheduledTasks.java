package com.bi.oranj.scheduler;

import com.bi.oranj.constant.Constants;
import com.bi.oranj.service.oranj.OranjService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by harshavardhanpatil on 5/31/17.
 */
@Component
public class ScheduledTasks {

    @Autowired
    OranjService oranjService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    @Scheduled(cron = "0 10 1 * * *")
    public void triggerGetGoals() {

        String yesterday = dateFormat.format(yesterday());
        log.info("Yesterday's date was {}", yesterday);
        oranjService.getGoals(yesterday);
        log.info("Saved {} goals", yesterday);
    }

    @Scheduled(cron = "0 30 2 * * *")
    public void triggerGetAUM (){
        log.info("Fetching AUM DATA");
        oranjService.fetchAUMData();
        log.info("Fetching AUM DATA: DONE");
    }


    /**
     * for now we stopped with this solution
     * but ideally, it should be done by moving data from actual tables to historical
     *
     * we considered the case of failure of this cron job. If it happens, we may lose
     * some historical data.
     *
     * this solution should be and will be improved !!!
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void triggerGetHistory(){
        log.info("Fetching AUM History");
        oranjService.fetchAUMHistory();
        log.info("Fetching AUM History: DONE");
    }



}
