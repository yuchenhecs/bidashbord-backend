package com.bi.oranj.service.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.GamificationSummary;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.GamificationRepository;
import com.bi.oranj.utils.InputValidator;
import com.bi.oranj.utils.date.DateUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static com.bi.oranj.constant.Constants.*;

@Service
public class GamificationService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    HttpServletResponse response;

    @Autowired
    AdvisorRepository advisorRepository;

    @Autowired
    GamificationRepository gamificationRepository;

    @Autowired
    InputValidator inputValidator;

    @Autowired
    DateUtility dateUtility;

    public RestResponse getAdvisorSummaryForGamification(Long advisorId) {
        try {
            if(advisorRepository.findById(advisorId) == null){
                return RestResponse.error("Advisor "+  advisorId +" does not exist");
            }
            GamificationSummary gamificationSummary = null;
            List<Object[]> gamificationResultSet = gamificationRepository.findByAdvisorIdAndDate(advisorId, dateUtility.getDate(1));
            for (Object[] resultSet : gamificationResultSet) {
                gamificationSummary = new GamificationSummary((((BigInteger) resultSet[0]).longValue()), ((String) resultSet[1] + " " + (String) resultSet[2]),
                        (((BigInteger) resultSet[3]).longValue()), ((BigDecimal) resultSet[4]),((BigDecimal) resultSet[5]),
                        ((BigDecimal) resultSet[6]), ((BigDecimal) resultSet[7]), ((BigDecimal) resultSet[8]),
                        ((Integer) resultSet[9]),((BigDecimal) resultSet[10]), ((BigDecimal) resultSet[11]),
                        ((BigDecimal) resultSet[12]), ((Integer) resultSet[13]), ((BigDecimal) resultSet[14]),
                        ((BigDecimal) resultSet[15]),((BigDecimal) resultSet[16]));
            }
            return RestResponse.successWithoutMessage(gamificationSummary);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_ADVISOR_SUMMARY + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_ADVISOR_SUMMARY);
        }
    }

    public RestResponse getAdvisorAchievements(Long advisorId, String region){
        try {

            return RestResponse.successWithoutMessage(null);
        } catch (Exception e) {
        log.error(ERROR_IN_GETTING_ACHIEVEMENTS + e);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return RestResponse.error(ERROR_IN_GETTING_ACHIEVEMENTS);
    }
    }
}
