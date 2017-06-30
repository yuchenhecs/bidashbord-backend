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

    public static final String ERROR_DATE_VALIDATION = "Date should be in 'yyyy-MM-dd' format";
    public static final String ERROR_USER_TYPE_VALIDATION = "'user' should be either 'prospect' or 'client'";
    public static final String ERROR_RANGE_TYPE_VALIDATION = "'range' should be either 'week' or 'month'";
    public static final String ERROR_PAGE_NUMBER_VALIDATION = "Page number should be a positive integer";
    public static final String ERROR_IN_GETTING_AUM = "Error in fecthing AUMs";
    public static final String ERROR_IN_GETTING_NET_WORTH = "Error in fetching net worth";
    public static final String ERROR_IN_GETTING_GOALS_FROM_ORANJ = "Error in fetching goals from Oranj";
    public static final String ERROR_IN_GETTING_DATE = "Error in Getting date";

}
