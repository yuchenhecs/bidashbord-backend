package com.bi.oranj.service.google.analytics;

import com.bi.oranj.utils.google.analytics.BiAnalytics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private static final String YESTERDAY = "yesterday";
    private static final int maxResults = 10000;
    private static final String[] dimensions = new String[]{"dimension4", "dimension5"};
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public String getThirtyDaysAnalyticsData (){
        Map<Boolean, List<List<String>>> resultContainer= biAnalytics.getResults("30daysAgo", YESTERDAY,
                "dimension1", 1, maxResults);
        return resultContainer.toString();
    }

    private String getPreciseOneMonthAnalyticsByThreeDays (){
        Calendar cal = GregorianCalendar.getInstance();
        int daysToDecrement = -2;
        int oneDayDecrement = -1;
        cal.add(Calendar.DATE, oneDayDecrement);
        int startIndex = -9999;

        Map<Boolean, List<List<String>>> resultContainer = null;
        List<List<String>> rawAnalytics = null;

        for (int j = 0; j < dimensions.length; j++){
            for (int i = 1; i <= 10; i++){
                String eDate = simpleDateFormat.format(cal.getTime());
                cal.add(Calendar.DATE, daysToDecrement);

                String sDate = simpleDateFormat.format(cal.getTime());
                cal.add(Calendar.DATE, oneDayDecrement);

                do{
                    resultContainer = biAnalytics.getResults(sDate, eDate,dimensions[j],
                            startIndex + maxResults, maxResults);
                    //!!!!!!!!!!! repository save
                    startIndex += maxResults;
                } while (!resultContainer.get(Boolean.TRUE).isEmpty());

                //!!!!!!!!!!! repository save
            }
        }

        return resultContainer.toString();
    }

    private String getPreciseOneMonthAnalyticsDayBy (){
        Calendar cal = GregorianCalendar.getInstance();
        int oneDayDecrement = -1;
        int startIndex = -9999;

        Map<Boolean, List<List<String>>> resultContainer = null;
        List<List<String>> rawAnalytics = null;

        for (int j = 0; j < dimensions.length; j++){
            for (int i = 1; i <= 30; i++){
                cal.add(Calendar.DATE, oneDayDecrement);
                String oneDayDate = simpleDateFormat.format(cal.getTime());

                do{
                    resultContainer = biAnalytics.getResults(oneDayDate, oneDayDate, dimensions[j],
                            startIndex + maxResults, maxResults);
                    //!!!!!!!!!!! repository save
                    startIndex += maxResults;
                } while (!resultContainer.get(Boolean.TRUE).isEmpty());

                //!!!!!!!!!!! repository save
            }
        }


        return resultContainer.toString();
    }

    public String getDailyAnalyticsData (){
        Map<Boolean, List<List<String>>> resultContainer = null;
        int
        for (int j = 0; j < dimensions.length; j++){
            biAnalytics.getResults(YESTERDAY, YESTERDAY, "dimension1",
                    1, 10000);
        }
        //!!!!!!! save into repository

        return resultContainer.toString();
    }
}
