package com.bi.oranj.scheduler;

import com.bi.oranj.model.bi.Cron;
import com.bi.oranj.repository.bi.CronRepository;
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

    @Autowired
    CronRepository cronRepository;


    private static final Logger log = LoggerFactory.getLogger(Scheduled.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }


    @Scheduled(cron = "0 05 1 * * *")
    public void fetchFirms() {
        Cron cron = new Cron();
        Date startTime = null;
        Date endTime = null;
        try{
            startTime = new Date();
            oranjService.getAllFirms();
            log.info("Cron Saved All Newly added Firms");
            endTime = new Date();
        } catch (Exception ex){
            cron.setErrorMessage(ex.toString());
        }finally {
            cron.setTaskName(getMethodName());
            cron.setStartTime(startTime);
            cron.setEndTime(endTime);
            cronRepository.save(cron);
        }
    }

    @Scheduled(cron = "0 10 1 * * *")
    public void fetchAdvisors() {

        Cron cron = new Cron();
        Date startTime = null;
        Date endTime = null;
        try{
            startTime = new Date();
            oranjService.getAllAdvisors();
            log.info("Cron Saved All Newly added Advisors");
            endTime = new Date();
        } catch (Exception ex){
            cron.setErrorMessage(ex.toString());
        }finally {
            cron.setTaskName(getMethodName());
            cron.setStartTime(startTime);
            cron.setEndTime(endTime);
            cronRepository.save(cron);
        }
    }

    @Scheduled(cron = "0 15 1 * * *")
    public void fetchClients() {
        Cron cron = new Cron();
        Date startTime = null;
        Date endTime = null;
        try{
            startTime = new Date();
            oranjService.getAllClients();
            log.info("Cron Saved All Newly added Clients");
            endTime = new Date();
        } catch (Exception ex){
            cron.setErrorMessage(ex.toString());
        }finally {
            cron.setTaskName(getMethodName());
            cron.setStartTime(startTime);
            cron.setEndTime(endTime);
            cronRepository.save(cron);
        }
    }

    @Scheduled(cron = "0 40 1 * * *")
    public void triggerGetGoals() {
        Cron cron = new Cron();
        Date startTime = null;
        Date endTime = null;
        try{
            startTime = new Date();
            String yesterday = dateFormat.format(yesterday());
            log.info("Yesterday's date was {}", yesterday);
            oranjService.getGoals(yesterday);
            log.info("Cron Saved {} goals", yesterday);
            endTime = new Date();
        } catch (Exception ex){
            cron.setErrorMessage(ex.toString());
        } finally {
            cron.setTaskName(getMethodName());
            cron.setStartTime(startTime);
            cron.setEndTime(endTime);
            cronRepository.save(cron);
        }
    }

    @Scheduled(cron = "0 30 2 * * *")
    public void triggerGetPositions (){

        Cron cron = new Cron();
        Date startTime = null;
        Date endTime = null;
        try{
            startTime = new Date();
            log.info("Fetching 'Positions' DATA");
            oranjService.fetchPositionsData();
            log.info("Fetching 'Positions' DATA: DONE");
            endTime = new Date();
        } catch (Exception ex){
            cron.setErrorMessage(ex.toString());
        }finally {
            cron.setTaskName(getMethodName());
            cron.setStartTime(startTime);
            cron.setEndTime(endTime);
            cronRepository.save(cron);
        }

    }

    @Scheduled(cron = "0 50 2 * * *")
    public void triggerGetNetWorth (){
        Cron cron = new Cron();
        Date startTime = null;
        Date endTime = null;
        try{
            startTime = new Date();
            String yesterday = dateFormat.format(yesterday());
            log.info("Fetching 'Net Worth' DATA");
            oranjService.getNetWorthForDate(yesterday);
            log.info("Fetching 'Net Worth' DATA: DONE");
            endTime = new Date();
        } catch (Exception ex){
            cron.setErrorMessage(ex.toString());
        }finally {
            cron.setTaskName(getMethodName());
            cron.setStartTime(startTime);
            cron.setEndTime(endTime);
            cronRepository.save(cron);
        }

    }

    @Scheduled (cron = "0 0 3 * * *")
    public void triggerGetAnalytics(){
        Cron cron = new Cron();
        Date startTime = null;
        Date endTime = null;
        try{
            startTime = new Date();
            log.info("Fetching 'Google Analytics' DATA");
            analyticsService.getAnalyticsDataForYesterday();
            log.info("Fetching 'Google Analytics' DATA: DONE");
            endTime = new Date();
        } catch (Exception ex){
            cron.setErrorMessage(ex.toString());
        }finally {
            cron.setTaskName(getMethodName());
            cron.setStartTime(startTime);
            cron.setEndTime(endTime);
            cronRepository.save(cron);
        }
    }

    private String getMethodName (){
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
