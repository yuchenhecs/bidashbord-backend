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
        log.info("Cron Saved {} goals", yesterday);
    }
}
