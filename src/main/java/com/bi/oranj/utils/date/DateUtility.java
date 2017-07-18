package com.bi.oranj.utils.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.bi.oranj.constant.Constants.*;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

@Component
public class DateUtility {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(YEAR_MONTH_DAY_FORMAT);

    public String getDate(int daysInThePast) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -daysInThePast);
        return dateFormat.format(cal.getTime());
    }

    public List<String> getQuarterFirstDates(){

        List<String> dateList = new ArrayList<>();
        long totalNumberOfQuarters = IsoFields.QUARTER_YEARS.between(
                LocalDate.of(START_YEAR, START_MONTH, START_DAY),
                LocalDate.of(Calendar.getInstance().get(Calendar.YEAR),
                        (Calendar.getInstance().get(Calendar.MONTH)+1),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)))+1;
        log.info("totalNumberOfQuarters ::: {}", totalNumberOfQuarters);

        LocalDate beginningYear = LocalDate.parse(START_YEAR + "-0" + START_MONTH + "-0" + START_DAY);
        LocalDate firstQuarter = beginningYear.with(firstDayOfYear());

        int monthsToAdd=0;
        for(int i=0; i<totalNumberOfQuarters; i++){
            dateList.add(firstQuarter.plusMonths(monthsToAdd).toString());
            monthsToAdd = monthsToAdd + 3;
        }
        dateList.add(LocalDate.now().toString());
        return dateList;
    }

    public List<String> getDates(List<String> dateRange, String range) {

        try{
            Calendar cal = Calendar.getInstance();
            switch (range){
                case WEEK:
                    cal.add(Calendar.DATE, -1);
                    dateRange.add(dateFormat.format(cal.getTime()));
                    cal.add(Calendar.DATE, -6);
                    dateRange.add(dateFormat.format(cal.getTime()));
                    break;
                case MONTH:
                    cal.add(Calendar.DATE, -1);
                    dateRange.add(dateFormat.format(cal.getTime()));
                    cal.add(Calendar.DATE, -29);
                    dateRange.add(dateFormat.format(cal.getTime()));
                    break;
                case TWO_WEEKS:
                    cal.add(Calendar.DATE, -8);
                    dateRange.add(dateFormat.format(cal.getTime()));
                    cal.add(Calendar.DATE, -6);
                    dateRange.add(dateFormat.format(cal.getTime()));
                    break;
                default:
                    break;
            }
        } catch (Exception e){
            log.error(ERROR_IN_GETTING_DATE + e);
        }
        return dateRange;
    }
}
