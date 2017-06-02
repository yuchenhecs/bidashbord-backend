package com.bi.oranj.service.oranj;

import com.bi.oranj.constant.CommonEnum;
import com.bi.oranj.model.bi.Advisor;
import com.bi.oranj.model.bi.BiGoal;
import com.bi.oranj.model.bi.Client;
import com.bi.oranj.model.bi.Firm;
import com.bi.oranj.model.oranj.OranjGoal;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.BiGoalRepository;
import com.bi.oranj.repository.bi.ClientRepository;
import com.bi.oranj.repository.bi.FirmRepository;
import com.bi.oranj.repository.oranj.OranjGoalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by harshavardhanpatil on 5/30/17.
 */
@Service
public class OranjService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OranjGoalRepository oranjGoalRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private FirmRepository firmRepository;

    @Autowired
    private BiGoalRepository biGoalRepository;

    /**
     * This method gets all the goals created between startDate and endDate
     * @param startDate
     * @param endDate
     * @return
     */
    public String getGoals(String startDate, String endDate){
        try{
            List<OranjGoal> oranjGoalList = oranjGoalRepository.FindByCreationDate(startDate, endDate);
            for(int i=0; i<oranjGoalList.size(); i++){

                Firm firm = new Firm();
                firm.setId(oranjGoalList.get(i).getFirmId());
                firm.setFirmName(oranjGoalList.get(i).getFirmName());
                firmRepository.save(firm);

                Advisor advisor = new Advisor();
                advisor.setId(oranjGoalList.get(i).getAdvisorId());
                advisor.setAdvisorFirstName(oranjGoalList.get(i).getAdvisorFirstName());
                advisor.setAdvisorLastName(oranjGoalList.get(i).getAdvisorLastName());
                advisorRepository.save(advisor);

                Client client = new Client();
                client.setId(oranjGoalList.get(i).getUser());
                client.setClientFirstName(oranjGoalList.get(i).getUserLastName());
                client.setClientLastName(oranjGoalList.get(i).getUserLastName());
                clientRepository.save(client);

                BiGoal biGoal = new BiGoal();
                biGoal.setGoalId(oranjGoalList.get(i).getId());
                biGoal.setName(oranjGoalList.get(i).getName());
                biGoal.setType(oranjGoalList.get(i).getType());
                biGoal.setCreationDate(oranjGoalList.get(i).getCreationDate());
                biGoal.setDeleted(oranjGoalList.get(i).isDeleted());
                biGoal.setFirmId(oranjGoalList.get(i).getFirmId());
                biGoal.setAdvisorId(oranjGoalList.get(i).getAdvisorId());
                biGoal.setUserId(oranjGoalList.get(i).getUser());
                biGoalRepository.save(biGoal);
            }
        }catch (Exception e){
            log.error("Error in fecthing goals from Oranj." + e);
        }
        return CommonEnum.SUCCESS.toString();
    }
}
