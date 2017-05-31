package com.bi.oranj.service.oranj;

import com.bi.oranj.constant.CommonEnum;
import com.bi.oranj.model.bi.BiGoal;
import com.bi.oranj.model.oranj.Advisor;
import com.bi.oranj.model.oranj.OranjGoal;
import com.bi.oranj.model.oranj.User;
import com.bi.oranj.repository.bi.BiGoalRepository;
import com.bi.oranj.repository.oranj.AdvisorRepository;
import com.bi.oranj.repository.oranj.FirmRepository;
import com.bi.oranj.repository.oranj.OranjGoalRepository;
import com.bi.oranj.repository.oranj.UserRepository;
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
    private UserRepository userRepository;

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
            List<OranjGoal> oranjGoalList = oranjGoalRepository.findByCreationDateBetween(startDate, endDate);
            saveGoals(oranjGoalList);
        }catch (Exception e){
            log.error("Error in fecthing goals from Oranj." + e);
        }
        return CommonEnum.SUCCESS.toString();
    }

    /**
     * This method returns User details for given user id
     * @param id
     * @return
     */
    public User getUserDetails(Long id){
        return userRepository.findById(id);
    }

    /**
     * This method returns Advisor details for given advisor id
     * @param advisorId
     * @return
     */
    public Advisor getAdvisorDetails(Long advisorId){
        return advisorRepository.findById(advisorId);
    }

    /**
     * This method returns Firm name for given firm id
     * @param firmId
     * @return
     */
    public String getFirmDetails(Long firmId){
        return firmRepository.findByFirm(firmId);
    }

    /**
     * This method checks if goals exists in bi_goals table & stores new goals.
     * @param oranjGoalList
     */
    public void saveGoals(List<OranjGoal> oranjGoalList){

        for (int i=0; i<oranjGoalList.size(); i++){
            BiGoal exist = biGoalRepository.findByGoalId(oranjGoalList.get(i).getId());
            if(exist==null){
                BiGoal biGoal = new BiGoal();
                biGoal.setName(oranjGoalList.get(i).getName());
                biGoal.setGoalId(oranjGoalList.get(i).getId());
                biGoal.setType(oranjGoalList.get(i).getType().toString());
                biGoal.setCreationDate(oranjGoalList.get(i).getCreationDate());
                biGoal.setUserId(oranjGoalList.get(i).getUser());
                biGoal.setDeleted(oranjGoalList.get(i).isDeleted());

                User user = getUserDetails(oranjGoalList.get(i).getUser());
                biGoal.setAdvisorId(user.getAdvisor());
                biGoal.setFirmId(user.getFirmId());
                biGoal.setUserFirstName(user.getFirstName());
                biGoal.setUserLastName(user.getLastName());

                Advisor advisor = getAdvisorDetails(biGoal.getAdvisorId());
                biGoal.setAdvisorFirstName(advisor.getFirstName());
                biGoal.setAdvisorLastName(advisor.getLastName());

                biGoal.setFirmName(getFirmDetails(biGoal.getFirmId()));
                biGoalRepository.save(biGoal);
            }
        }
    }
}
