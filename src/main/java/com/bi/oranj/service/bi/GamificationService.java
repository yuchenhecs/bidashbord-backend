package com.bi.oranj.service.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.Gamification;
import com.bi.oranj.model.bi.GamificationAdvisor;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.GamificationRankRepository;
import com.bi.oranj.repository.bi.GamificationRepository;
import com.bi.oranj.utils.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.bi.oranj.constant.Constants.*;

@Service
public class GamificationService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(YEAR_MONTH_DAY_FORMAT);

    HttpServletResponse response;

    @Autowired
    AdvisorRepository advisorRepository;

    @Autowired
    GamificationRepository gamificationRepository;

    @Autowired
    GamificationRankRepository gamificationRankRepository;

    @Autowired
    InputValidator inputValidator;

    public RestResponse getAdvisorSummaryForGamification(Long advisorId) {
        try {
            if(advisorRepository.findById(advisorId) == null){
                return RestResponse.error("Advisor "+  advisorId +" does not exist");
            }
            Gamification gamification = null;
            List<Object[]> gamificationResultSet = gamificationRepository.findByAdvisorIdAndDate(advisorId, getDate(1));
            for (Object[] resultSet : gamificationResultSet) {
                gamification = new Gamification((((BigInteger) resultSet[0]).longValue()), ((String) resultSet[1] + " " + (String) resultSet[2]),
                        (((BigInteger) resultSet[3]).longValue()), ((BigDecimal) resultSet[4]),((BigDecimal) resultSet[5]),
                        ((BigDecimal) resultSet[6]), ((BigDecimal) resultSet[7]), ((BigDecimal) resultSet[8]),
                        ((Integer) resultSet[9]),((BigDecimal) resultSet[10]), ((BigDecimal) resultSet[11]),
                        ((BigDecimal) resultSet[12]), ((Integer) resultSet[13]), ((BigDecimal) resultSet[14]),
                        ((BigDecimal) resultSet[15]),((BigDecimal) resultSet[16]));
            }
            return RestResponse.successWithoutMessage(gamification);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_ADVISOR_SUMMARY + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_ADVISOR_SUMMARY);
        }
    }

    public String getDate(int daysInThePast) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -daysInThePast);
        return dateFormat.format(cal.getTime());
    }
}
