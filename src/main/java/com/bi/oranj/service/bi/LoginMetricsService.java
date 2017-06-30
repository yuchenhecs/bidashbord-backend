package com.bi.oranj.service.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.*;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.AnalyticsRepository;
import com.bi.oranj.repository.bi.FirmRepository;
import com.bi.oranj.utils.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.bi.oranj.constant.Constants.*;

@Service
public class LoginMetricsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(YEAR_MONTH_DAY_FORMAT);

    HttpServletResponse response;

    @Autowired
    private InputValidator inputValidator;

    @Autowired
    private FirmRepository firmRepository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private AnalyticsRepository analyticsRepository;

    public RestResponse getLoginMetricsForAdmin(Integer pageNumber, String user, String range){

        List<String> dateRange = new ArrayList<>();
        Long roleId;

        if (!inputValidator.validateInputPageNumber(pageNumber)) {
            return RestResponse.error(ERROR_PAGE_NUMBER_VALIDATION);
        }

        if (!inputValidator.validateInputUserType(user)) {
            return RestResponse.error(ERROR_USER_TYPE_VALIDATION);
        } else {
            roleId = getRoleId(user);
        }

        if (!inputValidator.validateInputRangeType(range)) {
            return RestResponse.error(ERROR_RANGE_TYPE_VALIDATION);
        } else {
            dateRange = getDates(dateRange, range);
        }

        try {
            LoginMetricsForAdmin loginMetricsForAdmin = new LoginMetricsForAdmin();
            List<FirmLoginMetrics> firmLoginMetricsList = new ArrayList<>();
            Page<Firm> firmList = firmRepository.findByActiveTrue(new PageRequest(pageNumber, 100, Sort.Direction.ASC, "firmName"));
            for (int i=0; i<firmList.getContent().size(); i++){

                FirmLoginMetrics firmLoginMetrics = new FirmLoginMetrics();
                firmLoginMetrics.setFirmId(firmList.getContent().get(i).getId());
                firmLoginMetrics.setName(firmList.getContent().get(i).getFirmName());
                getLoginStats(firmLoginMetrics, roleId, dateRange.get(1), dateRange.get(0));
                firmLoginMetricsList.add(firmLoginMetrics);
            }
            loginMetricsForAdmin.setFirms(firmLoginMetricsList);
            loginMetricsForAdmin.setTotalFirms(firmList.getTotalElements());
            loginMetricsForAdmin.setHasNext(firmList.hasNext());
            loginMetricsForAdmin.setPage(pageNumber);
            loginMetricsForAdmin.setCount(firmLoginMetricsList.size());
            return RestResponse.successWithoutMessage(loginMetricsForAdmin);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_AUM + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_AUM);
        }
    }

    public RestResponse getLoginMetricsForFirm(Long firmId, Integer pageNumber, String user, String range){

        List<String> dateRange = new ArrayList<>();
        Long roleId;

        if (!inputValidator.validateInputPageNumber(pageNumber)) {
            return RestResponse.error(ERROR_PAGE_NUMBER_VALIDATION);
        }

        if (!inputValidator.validateInputUserType(user)) {
            return RestResponse.error(ERROR_USER_TYPE_VALIDATION);
        } else {
            roleId = getRoleId(user);
        }

        if (!inputValidator.validateInputRangeType(range)) {
            return RestResponse.error(ERROR_RANGE_TYPE_VALIDATION);
        } else {
            dateRange = getDates(dateRange, range);
        }

        try {
            LoginMetricsForFirm loginMetricsForFirm = new LoginMetricsForFirm();
            List<AdvisorLoginMetrics> advisorLoginMetricsList = new ArrayList<>();
            Page<Advisor> advisorList = advisorRepository.findByFirmIdAndActiveTrue(firmId, new PageRequest(pageNumber, 100, Sort.Direction.ASC, "advisorFirstName"));
            for (int i=0; i<advisorList.getContent().size(); i++){

                AdvisorLoginMetrics advisorLoginMetrics = new AdvisorLoginMetrics();
                advisorLoginMetrics.setAdvisorId(advisorList.getContent().get(i).getId());
                advisorLoginMetrics.setName(advisorList.getContent().get(i).getAdvisorFirstName() + " " + advisorList.getContent().get(i).getAdvisorLastName());
                getLoginStats(advisorLoginMetrics, roleId, dateRange.get(1), dateRange.get(0));
                advisorLoginMetricsList.add(advisorLoginMetrics);
            }
            loginMetricsForFirm.setAdvisors(advisorLoginMetricsList);
            loginMetricsForFirm.setTotalAdvisors(advisorList.getTotalElements());
            loginMetricsForFirm.setHasNext(advisorList.hasNext());
            loginMetricsForFirm.setPage(pageNumber);
            loginMetricsForFirm.setCount(advisorLoginMetricsList.size());
            return RestResponse.successWithoutMessage(loginMetricsForFirm);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_AUM + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_AUM);
        }
    }


    public void getLoginStats(FirmLoginMetrics firmLoginMetrics, Long roleId, String startDate, String endDate){

        BigInteger totalLogins = BigInteger.valueOf(0);
        Long uniqueLogins = 0l;
        BigDecimal totalSessionTime = BigDecimal.valueOf(0.0);
        List<Object[]> loginMetricsResultSet = analyticsRepository.findLoginMetricsForFirm(firmLoginMetrics.getFirmId(), roleId, startDate, endDate);
        for (Object[] resultSet : loginMetricsResultSet){
            totalLogins = totalLogins.add((BigInteger) resultSet[0]);
            uniqueLogins = uniqueLogins + 1;
            totalSessionTime = totalSessionTime.add((BigDecimal) resultSet[2]);
        }
        firmLoginMetrics.setTotalLogins(totalLogins);
        firmLoginMetrics.setUniqueLogins(uniqueLogins);
        if(totalLogins.compareTo(BigInteger.ZERO) == 0){
            firmLoginMetrics.setAvgSessionTime(totalSessionTime);
        }else {
            firmLoginMetrics.setAvgSessionTime(totalSessionTime
                    .divide(new BigDecimal(totalLogins), 2, RoundingMode.HALF_UP)
                    .divide(new BigDecimal(60), 2, RoundingMode.HALF_UP));
        }

    }

    public void getLoginStats(AdvisorLoginMetrics advisorLoginMetrics, Long roleId, String startDate, String endDate){

        BigInteger totalLogins = BigInteger.valueOf(0);
        Long uniqueLogins = 0l;
        BigDecimal totalSessionTime = BigDecimal.valueOf(0.0);
        List<Object[]> loginMetricsResultSet = analyticsRepository.findLoginMetricsForAdvisor(advisorLoginMetrics.getAdvisorId(), roleId, startDate, endDate);
        for (Object[] resultSet : loginMetricsResultSet){
            totalLogins = totalLogins.add((BigInteger) resultSet[0]);
            uniqueLogins = uniqueLogins + 1;
            totalSessionTime = totalSessionTime.add((BigDecimal) resultSet[2]);
        }
        advisorLoginMetrics.setTotalLogins(totalLogins);
        advisorLoginMetrics.setUniqueLogins(uniqueLogins);
        if(totalLogins.compareTo(BigInteger.ZERO) == 0){
            advisorLoginMetrics.setAvgSessionTime(totalSessionTime);
        }else {
            advisorLoginMetrics.setAvgSessionTime(totalSessionTime
                    .divide(new BigDecimal(totalLogins), 2, RoundingMode.HALF_UP)
                    .divide(new BigDecimal(60), 2, RoundingMode.HALF_UP));
        }
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
                default:
                    break;
            }
        } catch (Exception e){
            log.error(ERROR_IN_GETTING_DATE + e);
        }
        return dateRange;
    }

    public Long getRoleId(String role){

        Long roleId = 0l;
        switch (role){
            case CLIENT:
                roleId = 6l; break;
            case PROSPECT:
                roleId = 5l; break;
            default:
                break;
        }
        return roleId;
    }
}
