package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.*;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.AnalyticsRepository;
import com.bi.oranj.repository.bi.ClientRepository;
import com.bi.oranj.repository.bi.FirmRepository;
import com.bi.oranj.utils.ApiResponseMessage;
import com.bi.oranj.utils.InputValidator;
import com.bi.oranj.utils.date.DateUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    DateUtility dateUtility;

    public ResponseEntity<Object> getLoginMetricsForAdmin(Integer pageNumber, String user, String range){

        List<String> dateRange = new ArrayList<>();
        Long roleId;

        if (!inputValidator.validateInputPageNumber(pageNumber)) {
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_PAGE_NUMBER_VALIDATION), HttpStatus.BAD_REQUEST);
        }

        if (!inputValidator.validateInputUserType(user)) {
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_USER_TYPE_VALIDATION), HttpStatus.BAD_REQUEST);
        } else {
            roleId = getRoleId(user);
        }

        if (!inputValidator.validateInputRangeType(range)) {
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_RANGE_TYPE_VALIDATION), HttpStatus.BAD_REQUEST);
        } else {
            dateRange = dateUtility.getDates(dateRange, range);
        }

        try {
            LoginMetricsForAdmin loginMetricsForAdmin = new LoginMetricsForAdmin();
            List<FirmLoginMetrics> firmLoginMetricsList = new ArrayList<>();
            List<Object[]> loginMetricsResultSet = analyticsRepository.findLoginMetricsForAdmin(roleId, dateRange.get(1), dateRange.get(0));
            for (Object[] resultSet : loginMetricsResultSet) {

                FirmLoginMetrics firmLoginMetrics = new FirmLoginMetrics();
                firmLoginMetrics.setFirmId(((BigInteger) resultSet[0]).longValue());
                firmLoginMetrics.setName((String) resultSet[1]);
                if(resultSet[2] != null){
                    firmLoginMetrics.setTotalLogins(((BigInteger) resultSet[3]).longValue());
                    firmLoginMetrics.setUniqueLogins(((BigInteger) resultSet[4]).longValue());
                    firmLoginMetrics.setAvgSessionTime((((BigDecimal) resultSet[2]).divide(new BigDecimal(firmLoginMetrics.getTotalLogins()), 2, RoundingMode.HALF_UP).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP)).doubleValue());
                }else {
                    firmLoginMetrics.setTotalLogins(0l);
                    firmLoginMetrics.setUniqueLogins(0l);
                    firmLoginMetrics.setAvgSessionTime(0.0);
                }
                firmLoginMetricsList.add(firmLoginMetrics);
            }
            loginMetricsForAdmin.setUnit(MINUTE);
            loginMetricsForAdmin.setFirms(firmLoginMetricsList);
            loginMetricsForAdmin.setTotalFirms(firmLoginMetricsList.size());
            loginMetricsForAdmin.setHasNext(false);
            loginMetricsForAdmin.setPage(pageNumber);
            loginMetricsForAdmin.setCount(firmLoginMetricsList.size());
            return new ResponseEntity<>(loginMetricsForAdmin, HttpStatus.OK);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_LOGIN_METRICS, e);
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_IN_GETTING_LOGIN_METRICS), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> getLoginMetricsForFirm(Long firmId, Integer pageNumber, String user, String range){

        List<String> dateRange = new ArrayList<>();
        Long roleId;

        if (!inputValidator.validateInputPageNumber(pageNumber)) {
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_PAGE_NUMBER_VALIDATION), HttpStatus.BAD_REQUEST);
        }

        if (!inputValidator.validateInputUserType(user)) {
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_USER_TYPE_VALIDATION), HttpStatus.BAD_REQUEST);
        } else {
            roleId = getRoleId(user);
        }

        if (!inputValidator.validateInputRangeType(range)) {
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_RANGE_TYPE_VALIDATION), HttpStatus.BAD_REQUEST);
        } else {
            dateRange = dateUtility.getDates(dateRange, range);
        }

        try {
            LoginMetricsForFirm loginMetricsForFirm = new LoginMetricsForFirm();
            List<AdvisorLoginMetrics> advisorLoginMetricsList = new ArrayList<>();
            List<Object[]> loginMetricsResultSet = analyticsRepository.findLoginMetricsForFirm(firmId, roleId, dateRange.get(1), dateRange.get(0));
            for (Object[] resultSet : loginMetricsResultSet) {

                AdvisorLoginMetrics advisorLoginMetrics = new AdvisorLoginMetrics();
                advisorLoginMetrics.setAdvisorId(((BigInteger) resultSet[0]).longValue());
                advisorLoginMetrics.setName((String) resultSet[1] + " " + (String) resultSet[2]);
                if(resultSet[3] != null){
                    advisorLoginMetrics.setTotalLogins(((BigInteger) resultSet[4]).longValue());
                    advisorLoginMetrics.setUniqueLogins(((BigInteger) resultSet[5]).longValue());
                    advisorLoginMetrics.setAvgSessionTime((((BigDecimal) resultSet[3]).divide(new BigDecimal(advisorLoginMetrics.getTotalLogins()), 2, RoundingMode.HALF_UP).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP)).doubleValue());
                }else {
                    advisorLoginMetrics.setTotalLogins(0l);
                    advisorLoginMetrics.setUniqueLogins(0l);
                    advisorLoginMetrics.setAvgSessionTime(0.0);
                }
                advisorLoginMetricsList.add(advisorLoginMetrics);
            }
            loginMetricsForFirm.setUnit(MINUTE);
            loginMetricsForFirm.setAdvisors(advisorLoginMetricsList);
            loginMetricsForFirm.setTotalAdvisors(advisorLoginMetricsList.size());
            loginMetricsForFirm.setHasNext(false);
            loginMetricsForFirm.setPage(pageNumber);
            loginMetricsForFirm.setCount(advisorLoginMetricsList.size());
            return new ResponseEntity<>(loginMetricsForFirm, HttpStatus.OK);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_LOGIN_METRICS, e);
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_IN_GETTING_LOGIN_METRICS), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> getLoginMetricsForAdvisor(Long advisorId, Integer pageNumber, String user, String range){

        List<String> dateRange = new ArrayList<>();
        Long roleId;

        if (!inputValidator.validateInputPageNumber(pageNumber)) {
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_PAGE_NUMBER_VALIDATION), HttpStatus.BAD_REQUEST);
        }

        if (!inputValidator.validateInputUserType(user)) {
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_USER_TYPE_VALIDATION), HttpStatus.BAD_REQUEST);
        } else {
            roleId = getRoleId(user);
        }

        if (!inputValidator.validateInputRangeType(range)) {
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_RANGE_TYPE_VALIDATION), HttpStatus.BAD_REQUEST);
        } else {
            dateRange = dateUtility.getDates(dateRange, range);
        }

        try {
            LoginMetricsForAdvisor loginMetricsForAdvisor = new LoginMetricsForAdvisor();
            List<ClientLoginMetrics> clientLoginMetricsList = new ArrayList<>();
            List<Object[]> loginMetricsResultSet = analyticsRepository.findLoginMetricsForAdvisor(advisorId, roleId, dateRange.get(1), dateRange.get(0));
            for (Object[] resultSet : loginMetricsResultSet) {

                ClientLoginMetrics clientLoginMetrics = new ClientLoginMetrics();
                clientLoginMetrics.setClientId(((BigInteger) resultSet[0]).longValue());
                clientLoginMetrics.setName((String) resultSet[1] + " " + (String) resultSet[2]);
                if(resultSet[3] != null){
                    clientLoginMetrics.setTotalLogins(((BigInteger) resultSet[4]).longValue());
                    clientLoginMetrics.setUniqueLogins(((BigInteger) resultSet[5]).longValue());
                    clientLoginMetrics.setAvgSessionTime((((BigDecimal) resultSet[3]).divide(new BigDecimal(clientLoginMetrics.getTotalLogins()), 2, RoundingMode.HALF_UP).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP)).doubleValue());
                }else {
                    clientLoginMetrics.setTotalLogins(0l);
                    clientLoginMetrics.setUniqueLogins(0l);
                    clientLoginMetrics.setAvgSessionTime(0.0);
                }
                clientLoginMetricsList.add(clientLoginMetrics);
            }
            loginMetricsForAdvisor.setUnit(MINUTE);
            loginMetricsForAdvisor.setClients(clientLoginMetricsList);
            loginMetricsForAdvisor.setTotalClients(clientLoginMetricsList.size());
            loginMetricsForAdvisor.setHasNext(false);
            loginMetricsForAdvisor.setPage(pageNumber);
            loginMetricsForAdvisor.setCount(clientLoginMetricsList.size());
            return new ResponseEntity<>(loginMetricsForAdvisor, HttpStatus.OK);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_LOGIN_METRICS, e);
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_IN_GETTING_LOGIN_METRICS), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> getLoginMetricsSummary(String user){

        try {
            List<Object[]> loginMetricsResultSet1 = null;
            List<Object[]> loginMetricsResultSet2 = null;
            Long roleId;

            List<String> dateRange = new ArrayList<>();
            dateRange = dateUtility.getDates(dateRange, WEEK);
            LoginMetricsSummary loginMetricsSummary = new LoginMetricsSummary();
            Map<String, LoginMetricsSummary> map = new HashMap<>();

            dateRange = dateUtility.getDates(dateRange, TWO_WEEKS);


            if (!inputValidator.validateInputUserType(user)) {
                return new ResponseEntity<>(new ApiResponseMessage(ERROR_USER_TYPE_VALIDATION), HttpStatus.BAD_REQUEST);
            } else {
                roleId = getRoleId(user);
            }

            if (authorizationService.isSuperAdmin()) {
                loginMetricsResultSet1 = analyticsRepository.findLoginMetricsSummary(roleId, dateRange.get(1), dateRange.get(0));
                loginMetricsResultSet2 = analyticsRepository.findLoginMetricsSummary(roleId, dateRange.get(3), dateRange.get(2));
            } else if (authorizationService.isAdmin()){
                loginMetricsResultSet1 = analyticsRepository.findLoginMetricsSummaryForFirm(authorizationService.getUserId(),
                        roleId, dateRange.get(1), dateRange.get(0));
                loginMetricsResultSet2 = analyticsRepository.findLoginMetricsSummaryForFirm(authorizationService.getUserId(),
                        roleId, dateRange.get(3), dateRange.get(2));
            } else if (authorizationService.isAdvisor()){
                loginMetricsResultSet1 = analyticsRepository.findLoginMetricsSummaryForAdvisor(authorizationService.getUserId(),
                        roleId, dateRange.get(1), dateRange.get(0));
                loginMetricsResultSet2 = analyticsRepository.findLoginMetricsSummaryForAdvisor(authorizationService.getUserId(),
                        roleId, dateRange.get(3), dateRange.get(2));
            } else {
                return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }

            for (Object[] resultSet : loginMetricsResultSet1){
                if(resultSet[0] != null){
                    loginMetricsSummary.setTotalLogins((BigDecimal) resultSet[0]);
                    loginMetricsSummary.setUniqueLogins(new BigDecimal((BigInteger) resultSet[1]));
                    loginMetricsSummary.setAvgSessionTime(((BigDecimal) resultSet[2]).divide(loginMetricsSummary.getTotalLogins(), 2, RoundingMode.HALF_UP));
                } else {
                    loginMetricsSummary.setTotalLogins(BigDecimal.ZERO);
                    loginMetricsSummary.setUniqueLogins(BigDecimal.ZERO);
                    loginMetricsSummary.setAvgSessionTime(BigDecimal.ZERO);
                }
            }

            for (Object[] resultSet : loginMetricsResultSet2){
                if(resultSet[0] != null){
                    loginMetricsSummary.setChangeInTotalLogins(loginMetricsSummary.getTotalLogins().subtract(((BigDecimal) resultSet[0])));
                    loginMetricsSummary.setChangeInUniqueLogins(loginMetricsSummary.getUniqueLogins().subtract(new BigDecimal((BigInteger) resultSet[1])));
                    loginMetricsSummary.setChangeInAvgSessionTime(loginMetricsSummary.getAvgSessionTime().subtract((((BigDecimal) resultSet[2]).divide(loginMetricsSummary.getTotalLogins(), 2, RoundingMode.HALF_UP))));
                }else {
                    loginMetricsSummary.setChangeInTotalLogins(loginMetricsSummary.getTotalLogins().subtract(BigDecimal.ZERO));
                    loginMetricsSummary.setChangeInUniqueLogins(loginMetricsSummary.getUniqueLogins().subtract(BigDecimal.ZERO));
                    loginMetricsSummary.setChangeInAvgSessionTime(loginMetricsSummary.getAvgSessionTime().subtract(BigDecimal.ZERO));
                }
            }
            map.put(user, loginMetricsSummary);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_LOGIN_METRICS, e);
            return new ResponseEntity<>(new ApiResponseMessage(ERROR_IN_GETTING_LOGIN_METRICS), HttpStatus.BAD_REQUEST);
        }
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
