package com.bi.oranj.constant;

public class Constants {

    private Constants(){
        throw new IllegalStateException("Constants class");
    }

    public static final String SUCCESS = "SUCCESS";
    public static final String START_SECOND_OF_THE_DAY = "00:00:00";
    public static final String LAST_SECOND_OF_THE_DAY = "23:59:59";
    public static final String SPACE = " ";
    public static final int START_YEAR = 2014;
    public static final int START_MONTH = 1;
    public static final int START_DAY = 1;
    public static final String DATE_VALIDATION_ERROR = "Date should be in 'yyyy-MM-dd' format";
    public static final String PAGE_NUMBER_VALIDATION_ERROR = "Page number should be a positive integer";
    public static final String ERROR_IN_GETTING_AUM = "Error in fecthing AUMs";
    public static final String FIRM = "firm";
    public static final String ADVISOR = "advisor";
    public static final String CLIENT = "client";
}
