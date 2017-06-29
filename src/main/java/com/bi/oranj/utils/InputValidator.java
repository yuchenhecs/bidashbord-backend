package com.bi.oranj.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

import static com.bi.oranj.constant.Constants.*;

@Component
public class InputValidator {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public boolean validateInputDate(String previousDate, String currentDate){

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YEAR_MONTH_DAY_FORMAT);
            sdf.parse(previousDate);
            sdf.parse(currentDate);
        } catch (Exception e){
            log.error(ERROR_DATE_VALIDATION);
            return false;
        }
        return true;
    }

    public boolean validateInputPageNumber(Integer pageNumber) {
        return (pageNumber >= 0);
    }

    public boolean validateInputUserType(String user){
        return (user.equals(CLIENT) || user.equals(PROSPECT));
    }


    public boolean validateInputRangeType(String range){
        return (range.equals(WEEK) || range.equals(MONTH));
    }
}
