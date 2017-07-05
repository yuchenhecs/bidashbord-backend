package com.bi.oranj.scheduler;

import com.bi.oranj.service.google.analytics.BiAnalyticsService;
import com.bi.oranj.service.oranj.OranjService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class ScheduledTasks {

    @Autowired
    OranjService oranjService;

    @Autowired
    BiAnalyticsService analyticsService;


    private static final Logger log = LoggerFactory.getLogger(Scheduled.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    @Scheduled(cron = "0 40 1 * * *")
    public void triggerGetGoals() {
        String yesterday = dateFormat.format(yesterday());
        log.info("Yesterday's date was {}", yesterday);
        oranjService.getGoals(yesterday);
        log.info("Cron Saved {} goals", yesterday);
    }

    @Scheduled(cron = "0 05 1 * * *")
    public void fetchFirms() {
        oranjService.getAllFirms();
        log.info("Cron Saved All Newly added Firms");
    }

    @Scheduled(cron = "0 10 1 * * *")
    public void fetchAdvisors() {
        oranjService.getAllAdvisors();
        log.info("Cron Saved All Newly added Advisors");
    }

    @Scheduled(cron = "0 15 1 * * *")
    public void fetchClients() {
        oranjService.getAllClients();
        log.info("Cron Saved All Newly added Clients");
    }

    @Scheduled(cron = "0 30 2 * * *")
    public void triggerGetPositions (){
        log.info("Fetching 'Positions' DATA");
        oranjService.fetchPositionsData();
        log.info("Fetching 'Positions' DATA: DONE");
    }

    @Scheduled(cron = "0 50 2 * * *")
    public void triggerGetNetWorth (){
        String yesterday = dateFormat.format(yesterday());
        log.info("Fetching 'Net Worth' DATA");
        oranjService.getNetWorth(yesterday);
        log.info("Fetching 'Net Worth' DATA: DONE");
    }

    @Scheduled (cron = "0 3 00 * * *")
    public void triggerGetAnalytics(){
        log.info("Fetching 'Google Analytics' DATA");
        analyticsService.getAnalyticsDataForYesterday();
        log.info("Fetching 'Google Analytics' DATA: DONE");
    }
}
