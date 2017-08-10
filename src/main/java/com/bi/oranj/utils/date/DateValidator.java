package com.bi.oranj.utils.date;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jaloliddinbakirov on 6/14/17.
 */
@Component
public class DateValidator {

    private Pattern pattern;
    private Matcher matcher;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    private static final String DATE_PATTERN =
            "((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";

    public DateValidator(){
        pattern = Pattern.compile(DATE_PATTERN);
    }

    private boolean validatePattern(final String date){

        matcher = pattern.matcher(date);

        if(matcher.matches()){

            matcher.reset();

            if(matcher.find()){

                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") &&
                        (month.equals("4") || month .equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month .equals("06") ||
                                month.equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if(year % 4==0){
                        if(day.equals("30") || day.equals("31")){
                            return false;
                        }else{
                            return true;
                        }
                    }else{
                        if(day.equals("29")||day.equals("30")||day.equals("31")){
                            return false;
                        }else{
                            return true;
                        }
                    }
                }else{
                    return true;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private boolean isLessThanToday (String date){
        Date today = new Date();
        Date input = null;
        try{
            input = simpleDateFormat.parse(date);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return input.before(today);
    }

    public boolean validate (String date){
        return validatePattern(date) && isLessThanToday(date);
    }

    public boolean isLessOrEqual (String firstDate, String secondDate){
        Date first = null;
        Date second = null;
        try{
            first = simpleDateFormat.parse(firstDate);
            second = simpleDateFormat.parse(secondDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return first.before(second) || first.equals(second);
    }
}
