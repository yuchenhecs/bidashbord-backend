package com.bi.oranj.service.oranj;

import com.bi.oranj.constant.Constants;
import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.*;
import com.bi.oranj.model.oranj.OranjGoal;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.ClientRepository;
import com.bi.oranj.repository.bi.FirmRepository;
import com.bi.oranj.repository.bi.GoalRepository;
import com.bi.oranj.repository.oranj.OranjGoalRepository;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by harshavardhanpatil on 5/30/17.
 */
@Service
public class OranjService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    HttpServletResponse response;

    @Autowired
    private OranjGoalRepository oranjGoalRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private FirmRepository firmRepository;

    @Autowired
    private GoalRepository goalRepository;

    /**
     * This method gets all the goals created between startDate and endDate
     * @param date
     * @return
     */
    public RestResponse getGoals(String date){

        String startDate = date + Constants.SPACE + Constants.START_SECOND_OF_THE_DAY;
        String endDate = date + Constants.SPACE + Constants.LAST_SECOND_OF_THE_DAY;
        try{
            List<OranjGoal> oranjGoalList = oranjGoalRepository.FindByCreationDate(startDate, endDate);
            storeGoals(oranjGoalList);
        }catch (Exception e){
            log.error("Error in fecthing goals from Oranj." + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fecthing Goals from Oranj DB");
        }
        return RestResponse.success(date + " have been saved");
    }

    public RestResponse getGoalsTillDate(String date){

        String endDate = date + Constants.SPACE + Constants.LAST_SECOND_OF_THE_DAY;
        try{
            List<OranjGoal> oranjGoalList = oranjGoalRepository.FindGoalsTillDate(endDate);
            storeGoals(oranjGoalList);
        }catch (Exception e){
            log.error("Error in fecthing goals from Oranj." + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Error in fecthing Goals from Oranj DB");
        }
        return RestResponse.success("Goals created till" + date + " have been saved");
    }

    public void storeGoals(List<OranjGoal> oranjGoalList){
        try{
            for(int i=0; i<oranjGoalList.size(); i++){

                Firm firm = new Firm();
                firm.setId(oranjGoalList.get(i).getFirmId());
                firm.setFirmName(oranjGoalList.get(i).getFirmName());
                firmRepository.save(firm);

                Advisor advisor = new Advisor();
                advisor.setId(oranjGoalList.get(i).getAdvisorId());
                advisor.setAdvisorFirstName(oranjGoalList.get(i).getAdvisorFirstName());
                advisor.setAdvisorLastName(oranjGoalList.get(i).getAdvisorLastName());
                advisor.setFirmId(oranjGoalList.get(i).getFirmId());
                advisorRepository.save(advisor);

                Client client = new Client();
                client.setId(oranjGoalList.get(i).getUser());
                client.setClientFirstName(oranjGoalList.get(i).getUserLastName());
                client.setClientLastName(oranjGoalList.get(i).getUserLastName());
                client.setAdvisorId(oranjGoalList.get(i).getAdvisorId());
                client.setFirmId(oranjGoalList.get(i).getFirmId());
                clientRepository.save(client);

                BiGoal biGoal = new BiGoal();
                biGoal.setId(oranjGoalList.get(i).getId());
                biGoal.setName(oranjGoalList.get(i).getName());
                biGoal.setType(oranjGoalList.get(i).getType());
                biGoal.setGoalCreationDate(oranjGoalList.get(i).getCreationDate());
                biGoal.setDeleted(oranjGoalList.get(i).isDeleted());
                biGoal.setFirmId(oranjGoalList.get(i).getFirmId());
                biGoal.setAdvisorId(oranjGoalList.get(i).getAdvisorId());
                biGoal.setClientId(oranjGoalList.get(i).getUser());
//                DateTime currentTime = new DateTime();
//                biGoal.setInsertedOn(currentTime);
//                biGoal.setUpdatedOn(currentTime);
                goalRepository.save(biGoal);
            }
        }catch (Exception e){
            log.error("Error in storing goals in BI DB" + e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
