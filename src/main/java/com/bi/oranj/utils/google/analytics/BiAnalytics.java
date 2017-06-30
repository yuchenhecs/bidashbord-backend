package com.bi.oranj.utils.google.analytics;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analytics.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */

@Component
public class BiAnalytics {

    private final Logger logger = LoggerFactory.getLogger(BiAnalytics.class);

    @Value(value = "classpath:google.analytics/bi-key.p12")
    private File key;

    private static final String APPLICATION_NAME = "analytics-project";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACCOUNT_EMAIL = "oranj-bi-analytics@core-theme-171519.iam.gserviceaccount.com";

    private static Analytics analytics = null;

    public Map<Boolean, List<List<String>>> getResults(String startDate, String endDate, String dimensionName, Integer startIndex, Integer maxResults){

        String dimensions = "ga:sessionDurationBucket, ga:" + dimensionName + ", ga:dateHourMinute";
        String metrics = "ga:users";
        String sortBy = "ga:" + dimensionName + ",ga:dateHourMinute";
        Map<Boolean, List<List<String>>> resultContainer = new HashMap<>();

        List<List<String>> rows = null;
        try {
            if (analytics == null)
                analytics = initializeAnalytics();

            String profile = getFirstProfileId();
            GaData gaData = queryAPI(profile, startDate, endDate, metrics, dimensions, startIndex, maxResults, sortBy);

            if (gaData != null && !gaData.getRows().isEmpty()) {
                rows = gaData.getRows();

                if (gaData.getNextLink() != null)
                    resultContainer.put(true, rows);
                else
                    resultContainer.put (false, rows);

            }

        } catch (Exception ex){
            logger.error("Error occured while getting Google Analytics data between dates: " + startDate + "-" + endDate);
        }

        return resultContainer;
    }

    private Analytics initializeAnalytics() throws Exception {
        // Initializes an authorized analytics service object.

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
                .setServiceAccountPrivateKeyFromP12File(key)
                .setServiceAccountScopes(AnalyticsScopes.all())
                .build();

        // Construct the Analytics service object.
        return new Analytics.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
    }


    private String getFirstProfileId() throws Exception {
        // Get the first view (profile) ID for the authorized user.
        String profileId = null;

        // Query for the list of all accounts associated with the service account.
        Accounts accounts = analytics.management().accounts().list().execute();

        if (accounts.getItems().isEmpty()) {
            logger.error("Google Analytic: No accounts found");
            throw new Exception("Google Analytic: No accounts found");
        } else {
            String oranjAccountId = "";

            for (Account account : accounts.getItems()){
                if (account.getName().equalsIgnoreCase("oranj"))
                    oranjAccountId = account.getId();
            }

            if (oranjAccountId.equals("")) {
                logger.error("Google Analytics: No Oranj account found in accounts list");
                throw new Exception("Google Analytics: No Oranj account found in accounts list");
            }

            // Query for the list of properties associated with the first account.
            Webproperties properties = analytics.management().webproperties()
                    .list(oranjAccountId).execute();

            if (properties.getItems().isEmpty()) {
                logger.error("Google Analytics: No Webproperties found");
                throw new Exception("Google Analytics: No Webproperties found");
            } else {
                String webPropertyId = "";

                for (Webproperty webproperty : properties.getItems()){
                    if (webproperty.getName().equalsIgnoreCase("reskin"))
                        webPropertyId = webproperty.getId();
                }

                if (webPropertyId.equals("")){
                    logger.error("Google Analytics: No Reskin webproperties found in webproperties list");
                    throw new Exception("Google Analytics: No Reskin webproperties found in webproperties list");
                }

                // Query for the list views (profiles) associated with the property.
                Profiles profiles = analytics.management().profiles()
                        .list(oranjAccountId, webPropertyId).execute();

                if (profiles.getItems().isEmpty()) {
                    System.err.println("No views (profiles) found");
                    logger.error("No views (profiles) found");
                    new Exception("No views (profiles) found");
                } else {
                    // Return the first (view) profile associated with the property.
                    // returning "All Web Site Data" view
                    profileId = profiles.getItems().get(0).getId();
                }
            }
        }
        return profileId;
    }

    private GaData queryAPI(String profileId, String startDate, String endDate,
                            String metrics, String dimensions, Integer startIndex, Integer maxResults, String sortBy) throws IOException {
        return analytics.data().ga()
                .get("ga:" + profileId, startDate, endDate, metrics)
                .setDimensions(dimensions)
                .setSort(sortBy)
//                .setFilters("ga:dimension1==801")
                .setOutput("json")
                .setPrettyPrint(true)
                .setSamplingLevel("HIGHER_PRECISION")
                .setIncludeEmptyRows(false)
                .setStartIndex(startIndex)
                .setMaxResults(maxResults)
                .execute();

    }

}
