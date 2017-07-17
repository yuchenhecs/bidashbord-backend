package com.bi.oranj.service.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.GamificationAdvisor;
import com.bi.oranj.model.bi.GamificationCategories;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.GamificationRankRepository;
import com.bi.oranj.repository.bi.GamificationRepository;
import com.bi.oranj.utils.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
            GamificationCategories gamificationCategories = gamificationRepository.findByAdvisorIdAndDate(advisorId, getDate(1));
            if(gamificationCategories == null){
                gamificationCategories = gamificationRepository.findByAdvisorIdAndDate(advisorId, getDate(2));
            }
            return RestResponse.successWithoutMessage(gamificationCategories);
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

    public RestResponse getAdvisorRankForGamification(Long advisorId) {

        try{
            if(advisorRepository.findById(advisorId) == null){
                return RestResponse.error("Advisor "+  advisorId +" does not exist");
            }
            GamificationAdvisor gamificationAdvisor = gamificationRankRepository.findByAdvisorIdAndDate(advisorId, getDate(1));
            if(gamificationAdvisor == null){
                gamificationAdvisor = gamificationRankRepository.findByAdvisorIdAndDate(advisorId, getDate(2));
            }
            return RestResponse.successWithoutMessage(gamificationAdvisor);
        }catch (Exception e) {
            log.error(ERROR_IN_GETTING_ADVISOR_RANK + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_ADVISOR_RANK);
        }
    }
}
