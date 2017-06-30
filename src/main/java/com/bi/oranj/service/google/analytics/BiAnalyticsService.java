package com.bi.oranj.service.google.analytics;

import com.bi.oranj.model.bi.Analytics;
import com.bi.oranj.repository.bi.AnalyticsRepository;
import com.bi.oranj.repository.bi.RoleRepository;
import com.bi.oranj.utils.DummyService;
import com.bi.oranj.utils.google.analytics.BiAnalytics;
import jdk.nashorn.internal.runtime.ECMAException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.java2d.pipe.SpanShapeRenderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * dimension 4 - prospect
 * dimension 5 - client
 */
@Service
public class BiAnalyticsService {
    @Autowired
    private BiAnalytics biAnalytics;

    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DummyService dummyService; // ! this code only for test purposes, to create dummy data for given Ids

    private final Logger logger = LoggerFactory.getLogger(BiAnalyticsService.class);
    private static final int maxResults = 10000;
    private static final SimpleDateFormat simpleDateFormatQuery = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat simpleDateFormatSave = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Map<String, String> dimensions;
    static {
        dimensions  = new HashMap<>();
        dimensions.put("dimension4", "ROLE_PROSPECT");
        dimensions.put("dimension5", "ROLE_CLIENT");
    }


    public void getPreciseOneMonthAnalyticsDayBy (){
        Calendar cal = GregorianCalendar.getInstance();
        int oneDayDecrement = -1;
        int startIndex = -9999;
        Long ROLE_ID = 0l;

        List<List<String>> resultsRaw = null;
        Map<Boolean, List<List<String>>> resultContainer = null;
        List<List<String>> results = null;

        for (Map.Entry e : dimensions.entrySet()){
            ROLE_ID = roleRepository.getRoleId((String)e.getValue());
            results = new ArrayList<>();

            for (int i = 1; i <= 30; i++){
                cal.add(Calendar.DATE, oneDayDecrement);
                String oneDayDate = simpleDateFormatQuery.format(cal.getTime());

                do{
                    resultContainer = biAnalytics.getResults(oneDayDate, oneDayDate, (String)e.getKey(),
                            startIndex + maxResults, maxResults);
                    resultsRaw = resultContainer.get(Boolean.TRUE) == null ? resultContainer.get(Boolean.FALSE) : resultContainer.get(Boolean.TRUE);

                    if (resultsRaw == null){
                        logger.error(String.format("There is no data for the give date - %s in Google Analytics" +
                                " for %s metrics", oneDayDate, (String)e.getValue()));
                        continue;
                    }

                    results.addAll(resultsRaw);
                    startIndex += maxResults;
                } while (resultContainer.get(Boolean.TRUE) != null);

                startIndex = -9999;
                save(results, ROLE_ID, cal.getTime());
            }
        }
    }

    public String getAnalyticsDataForYesterday (){
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = simpleDateFormatQuery.format(cal.getTime());
        Long ROLE_ID = 0l;

        Map<Boolean, List<List<String>>> resultContainer = null;
        List<List<String>> results = null;
        List<List<String>> resultsRaw = null;
        List<Analytics> analyticsList = new ArrayList<>();
        int startIndex = -9999;

        for (Map.Entry e : dimensions.entrySet()){
            ROLE_ID = roleRepository.getRoleId((String)e.getValue());
            results = new ArrayList<>();
            do{
                resultContainer = biAnalytics.getResults(yesterday, yesterday, (String)e.getKey(),
                        startIndex + maxResults, maxResults);

                resultsRaw = resultContainer.get(Boolean.TRUE) == null ? resultContainer.get(Boolean.FALSE) : resultContainer.get(Boolean.TRUE);

                if (resultsRaw == null){
                    logger.error(String.format("There is no data for the give date - %s in Google Analytics" +
                            " for %s metrics", yesterday, (String)e.getValue()));
                    continue;
                }

                results.addAll(resultsRaw);
                startIndex += maxResults;
            }while (resultContainer.get(Boolean.TRUE) != null);

            startIndex = -9999;
            analyticsList.addAll(save(results, ROLE_ID, cal.getTime()));
        }

        return analyticsList.toString();
    }

    private List<Analytics> save(List<List<String>> results, Long roleId, Date createdOn) {
        Set<Long> clientIds = new TreeSet<>(); // ! this code only for test purposes, to create dummy data for given Ids

        if (results == null) return null;

        List<Analytics> analyticsList = new ArrayList<>();

        Long previousId = 0l;
        Long previousSessionStart = 0l;
        for (List<String> object : results){
            Long currentId = Long.valueOf(object.get(1));
            Long sessionStart = Long.valueOf(object.get(2));

            clientIds.add(currentId);   // ! this code only for test purposes, to create dummy data for given Ids

            if ((previousId == 0) || (!previousId.equals(currentId)) || (previousId.equals(currentId) && sessionStart >= previousSessionStart)){
                Analytics analytics = new Analytics();
                analytics.setClientId(currentId);
                analytics.setSessionStartDate(convertToDate(sessionStart));
                analytics.setRoleId(roleId);
                analytics.setCreatedOn(createdOn);
                analytics.setSessionDuration(Integer.parseInt(object.get(0)));
                analyticsList.add(analytics);

                previousId = currentId;
                previousSessionStart = sessionStart + 100;
            }
        }

        dummyService.createData(8, 13, clientIds); // ! this code only for test purposes, to create dummy data for given Ids


        try {
            analyticsRepository.save(analyticsList);
        }catch (Exception e){
            logger.error("Error occured while saving analytics data", e);
        }

        return analyticsList;
    }

    private Date convertToDate (Long raw){
        String rawDate = String.valueOf(raw);
        String year = rawDate.substring(0,4);
        String month = rawDate.substring(4, 6);
        String day = rawDate.substring(6, 8);
        String hour = rawDate.substring(8, 10);
        String minute = rawDate.substring(10, 12);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(year).append("-").append(month).append("-").append(day).append(" ").append(hour)
                .append(":").append(minute).append(":").append("00");
        Date date = null;
        try{
            date = simpleDateFormatSave.parse(stringBuilder.toString());
        } catch (ParseException e) {
            logger.error("Error occured while converting date from Google Analytics to Date()");
        }

        return date;
    }


}
