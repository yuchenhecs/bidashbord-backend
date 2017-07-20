package com.bi.oranj.constant;

public class Constants {

    private Constants(){
        throw new IllegalStateException("Constants class");
    }

    public static final String YEAR_MONTH_DAY_FORMAT = "yyyy-MM-dd";
    public static final int START_YEAR = 2014;
    public static final int START_MONTH = 1;
    public static final int START_DAY = 1;
    public static final String FIRM = "firm";
    public static final String ADVISOR = "advisor";
    public static final String CLIENT = "client";
    public static final String PROSPECT = "prospect";
    public static final String WEEK = "week";
    public static final String MONTH = "month";
    public static final String TWO_WEEKS = "twoWeeks";
    public static final String MINUTE = "minute";
    public static final String OVERALL = "overall";
    public static final String STATE = "state";

    public static final String RANK_ONE = "RANK_ONE";
    public static final String RANK_TWO = "RANK_TWO";
    public static final String RANK_THREE = "RANK_THREE";
    public static final String TOP_X = "TOP_X";
    public static final String ABOVE_AVG = "ABOVE_AVG";
    public static final String NO_RANK = "NO_RANK";

    public static final String AUM = "AUM";
    public static final String NET_WORTH = "NET WORTH";
    public static final String HNI = "HNI";
    public static final String CONVERSION_RATE = "CONVERSION RATE";
    public static final String AVG_CONVERSION_TIME = "AVG CONVERSION TIME";
    public static final String RETENTION_RATE = "RETENTION RATE";
    public static final String WEEKLY_CLIENT_LOGINS = "WEEKLY CLIENT LOGINS";
    public static final String AUM_GROWTH = "AUM GROWTH";
    public static final String NET_WORTH_GROWTH = "NET WORTH GROWTH";
    public static final String CLIENTELE_GROWTH = "CLIENTELE GROWTH";

    public static final String ERROR_DATE_VALIDATION = "Date should be in 'yyyy-MM-dd' format";
    public static final String ERROR_USER_TYPE_VALIDATION = "'user' should be either 'prospect' or 'client'";
    public static final String ERROR_REGION_VALIDATION = "'region' should be either 'overall' or 'state' or 'firm'";
    public static final String ERROR_RANGE_TYPE_VALIDATION = "'range' should be either 'week' or 'month'";
    public static final String ERROR_PAGE_NUMBER_VALIDATION = "Page number should be a positive integer";
    public static final String ERROR_IN_GETTING_AUM = "Error in fetching AUMs";
    public static final String ERROR_IN_GETTING_LOGIN_METRICS = "Error in fetching Login Metrics";
    public static final String ERROR_IN_GETTING_ADVISOR_SUMMARY = "Error in getting advisor summary";
    public static final String ERROR_IN_GETTING_ACHIEVEMENTS = "Error in getting advisor achievements";
    public static final String ERROR_IN_GETTING_NET_WORTH = "Error in fetching net worth";
    public static final String ERROR_IN_GETTING_GOALS_FROM_ORANJ = "Error in fetching goals from Oranj";
    public static final String ERROR_IN_GETTING_DATE = "Error in Getting date";

}
