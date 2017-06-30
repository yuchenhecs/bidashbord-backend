package com.bi.oranj.service.google.analytics;

import com.bi.oranj.model.bi.Analytics;
import com.bi.oranj.repository.bi.AnalyticsRepository;
import com.bi.oranj.repository.bi.RoleRepository;
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

    private final Logger logger = LoggerFactory.getLogger(BiAnalyticsService.class);
    private Long ROLE_ID = 0l;
    private static final String YESTERDAY = "yesterday";
    private static final int maxResults = 10000;
    private static final SimpleDateFormat simpleDateFormatQuery = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat simpleDateFormatSave = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Map<String, String> dimensions;
    static {
        dimensions  = new HashMap<>();
        dimensions.put("dimension4", "ROLE_PROSPECT");
        dimensions.put("dimension5", "ROLE_CLIENT");
    }


    public String getPreciseOneMonthAnalyticsDayBy (){
        Calendar cal = GregorianCalendar.getInstance();
        int oneDayDecrement = -1;
        int startIndex = -9999;

        Map<Boolean, List<List<String>>> resultContainer = null;
        List<List<String>> results = null;
        List<Analytics> analyticsList = null;

        for (Map.Entry e : dimensions.entrySet()){
            if (ROLE_ID == null || ROLE_ID == 0)
                ROLE_ID = roleRepository.getRoleId((String)e.getValue()).getId();

            for (int i = 1; i <= 30; i++){
                cal.add(Calendar.DATE, oneDayDecrement);
                String oneDayDate = simpleDateFormatQuery.format(cal.getTime());

                do{
                    resultContainer = biAnalytics.getResults(oneDayDate, oneDayDate, (String)e.getKey(),
                            startIndex + maxResults, maxResults);

                    results = resultContainer.get(Boolean.TRUE) == null ? resultContainer.get(Boolean.FALSE) : resultContainer.get(Boolean.TRUE);
                    analyticsList.addAll(save(results, ROLE_ID));
                    startIndex += maxResults;
                } while (resultContainer.get(Boolean.TRUE) != null);

            }
        }

        return analyticsList.toString();
    }

    public String getAnalyticsDataForYesterday (){
        Map<Boolean, List<List<String>>> resultContainer = null;
        List<List<String>> results = null;
        List<Analytics> analyticsList = null;
        int startIndex = -9999;

        for (Map.Entry e : dimensions.entrySet()){
            if (ROLE_ID == null || ROLE_ID == 0)
                ROLE_ID = roleRepository.getRoleId((String)e.getValue()).getId();

            do{
                resultContainer = biAnalytics.getResults(YESTERDAY, YESTERDAY, (String)e.getKey(),
                        startIndex + maxResults, maxResults);

                results = resultContainer.get(Boolean.TRUE) == null ? resultContainer.get(Boolean.FALSE) : resultContainer.get(Boolean.TRUE);
                analyticsList.addAll(save(results, ROLE_ID));
                startIndex += maxResults;
            }while (resultContainer.get(Boolean.TRUE) != null);

        }

        return analyticsList.toString();
    }

    private List<Analytics> save(List<List<String>> results, Long roleId) {

        List<Analytics> analyticsList = new ArrayList<>();

        Long previousId = 0l;
        Long previousSessionStart = 0l;
        for (List<String> object : results){
            Long currentId = Long.valueOf(object.get(1));
            Long sessionStart = Long.valueOf(results.get(0).get(2));

            if (previousId == 0 || previousId != currentId || (previousId == currentId && sessionStart >= previousSessionStart)){
                Analytics analytics = new Analytics();
                analytics.setClientId(currentId);
                analytics.setSessionStartDate(convertToDate(sessionStart));
                analytics.setRoleId(roleId);
                analyticsList.add(analytics);

                previousId = currentId;
                previousSessionStart = sessionStart + 100;
            }
        }

        try {
            analyticsRepository.save(analyticsList);
        }catch (Exception e){
            logger.error("Error occured while saving analytics data");
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
