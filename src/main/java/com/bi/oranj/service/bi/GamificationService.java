package com.bi.oranj.service.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.GamificationSummary;
import com.bi.oranj.model.bi.PatOnTheBack;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.GamificationRepository;
import com.bi.oranj.repository.bi.PatOnTheBackRepository;
import com.bi.oranj.utils.InputValidator;
import com.bi.oranj.utils.date.DateUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    PatOnTheBackRepository patOnTheBackRepository;

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
            if (!inputValidator.validateInputRegion(region)) {
                return RestResponse.error(ERROR_REGION_VALIDATION);
            }
            if(advisorRepository.findById(advisorId) == null){
                return RestResponse.error("Advisor "+  advisorId +" does not exist");
            }
            PatOnTheBack patOnTheBack = null;
            List<Object[]> patOnTheBackResultSet = patOnTheBackRepository.findByAdvisorIdRegionAndDate(advisorId, region.toUpperCase() ,dateUtility.getDate(1));
            for (Object[] resultSet : patOnTheBackResultSet) {
                Map<String, String> sentenceMap = getSentences(resultSet, region);
                patOnTheBack = new PatOnTheBack(((BigInteger) resultSet[0]).longValue(), ((BigInteger) resultSet[1]).longValue(),
                        resultSet[2].toString().toLowerCase(), sentenceMap.get(AUM), sentenceMap.get(NET_WORTH), sentenceMap.get(HNI),
                        sentenceMap.get(CONVERSION_RATE), sentenceMap.get(AVG_CONVERSION_TIME), sentenceMap.get(RETENTION_RATE),
                        sentenceMap.get(WEEKLY_CLIENT_LOGINS), sentenceMap.get(AUM_GROWTH), sentenceMap.get(NET_WORTH_GROWTH),
                        sentenceMap.get(CLIENTELE_GROWTH));
            }
            return RestResponse.successWithoutMessage(patOnTheBack);
        } catch (Exception e) {
            log.error(ERROR_IN_GETTING_ACHIEVEMENTS + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error(ERROR_IN_GETTING_ACHIEVEMENTS);
        }
    }

    public String formSentence(String value, String kpi, String region){

        String sentence = null;
        switch (value){
            case RANK_ONE:
                sentence = "You are Ranked 1st in "+ kpi +" among all advisors in the " + region;
                break;
            case RANK_TWO:
                sentence = "You are Ranked 2nd in "+ kpi +" among all advisors in the " + region;
                break;
            case RANK_THREE:
                sentence = "You are Ranked 3rd in "+ kpi +" among all advisors in the " + region;
                break;
            case TOP_X:
                sentence = "You are among top 40% of the advisors in "+ kpi +" in the " + region;
                break;
            case ABOVE_AVG:
                sentence = "Your "+ kpi +" is above the " + region + " average";
                break;
            default:
                break;
        }
        return sentence;
    }

    public Map<String, String> getSentences(Object[] resultSet, String region){

        Map<String, String> sentenceMap = new HashMap<>();
        String aum = null; String netWorth = null; String hni = null; String conversionRate = null; String avgConversionTime = null; String retentionRate = null;
        String weeklyLogins = null; String aumGrowth = null; String netWorthGrowth = null; String clienteleGrowth = null;
        if(!resultSet[3].equals(NO_RANK)){
            aum = formSentence((String) resultSet[3],AUM, region);
            sentenceMap.put(AUM, aum);
        }
        if(!resultSet[4].equals(NO_RANK)){
            netWorth = formSentence((String) resultSet[4],NET_WORTH, region);
            sentenceMap.put(NET_WORTH, netWorth);
        }
        if(!resultSet[5].equals(NO_RANK)){
            hni = formSentence((String) resultSet[5],HNI, region);
            sentenceMap.put(HNI, hni);
        }
        if(!resultSet[6].equals(NO_RANK)){
            conversionRate = formSentence((String) resultSet[6],CONVERSION_RATE, region);
            sentenceMap.put(CONVERSION_RATE, conversionRate);
        }
        if(!resultSet[7].equals(NO_RANK)){
            avgConversionTime = formSentence((String) resultSet[7],AVG_CONVERSION_TIME, region);
            sentenceMap.put(AVG_CONVERSION_TIME, avgConversionTime);
        }
        if(!resultSet[8].equals(NO_RANK)){
            retentionRate = formSentence((String) resultSet[8],RETENTION_RATE, region);
            sentenceMap.put(RETENTION_RATE, retentionRate);
        }
        if(!resultSet[9].equals(NO_RANK)){
            weeklyLogins = formSentence((String) resultSet[9],WEEKLY_CLIENT_LOGINS, region);
            sentenceMap.put(WEEKLY_CLIENT_LOGINS, weeklyLogins);
        }
        if(!resultSet[10].equals(NO_RANK)){
            aumGrowth = formSentence((String) resultSet[10],AUM_GROWTH, region);
            sentenceMap.put(AUM_GROWTH, aumGrowth);
        }
        if(!resultSet[11].equals(NO_RANK)){
            netWorthGrowth = formSentence((String) resultSet[11],NET_WORTH_GROWTH, region);
            sentenceMap.put(NET_WORTH_GROWTH, netWorthGrowth);
        }
        if(!resultSet[12].equals(NO_RANK)){
            clienteleGrowth = formSentence((String) resultSet[12],CLIENTELE_GROWTH, region);
            sentenceMap.put(CLIENTELE_GROWTH, clienteleGrowth);
        }
        return sentenceMap;
    }
}
