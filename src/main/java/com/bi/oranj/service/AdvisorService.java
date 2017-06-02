package com.bi.oranj.service;

import com.bi.oranj.entity.BiGoal;
import com.bi.oranj.entity.GoalEntity;
import com.bi.oranj.json.GoalResponse;
import com.bi.oranj.repository.AdvisorRepository;
import com.bi.oranj.repository.GoalRepository;
import com.bi.oranj.wrapper.user.Advisor;
import com.bi.oranj.wrapper.user.Firm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by jaloliddinbakirov on 5/24/17.
 */
@Service
public class AdvisorService {

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Value("${page.size}")
    private Integer pageSize;


    public int totalPages (long firmId){
        return advisorRepository.findDistinct(firmId).size() / 10;
    }

    public Collection<Advisor> findAdvisorsOrdered (int pageNum, long firmId){

        List<Object[]> goalObjects = (List<Object[]>) advisorRepository.findGoalsOrdered(firmId, pageNum * 10, pageSize);

        Map<Integer, Advisor> hashMap = new HashMap<>();

        for (Object[] goal : goalObjects){
            int advisorId = ((BigInteger) goal[0]).intValue();
            String firstName = (String) goal[1];
            String lastName = (String) goal[2];
            String[] types = ((String) goal[3]).split(",");
            int count = ((BigInteger) goal[4]).intValue();

            if (hashMap.containsKey(advisorId)){
                Advisor advisor = hashMap.get(advisorId);

                HashMap<String, Integer> goalList = advisor.getGoals();
                for (String s : types){
                    s = s.toLowerCase();
                    if (goalList.containsKey(s)){
                        goalList.put(s, goalList.get(s) + 1);
                    }else {
                        goalList.put(s, 1);
                    }
                }

                advisor.setGoals(goalList);
            } else {
                HashMap<String, Integer> goalList = new HashMap<>();
                for (String s : types){
                    s = s.toLowerCase();
                    if (goalList.containsKey(s)){
                        goalList.put(s, goalList.get(s) + 1);
                    }else {
                        goalList.put(s, 1);
                    }
                }
                hashMap.put(advisorId, new Advisor(advisorId, firstName, lastName, goalList, count));
            }
        }


        return hashMap.values();
    }

    public GoalResponse buildResponse (int pageNum, long firmId){
        int totalAdvisors = advisorRepository.findDistinct(firmId).size();
        int totalGoals = goalRepository.totalAdvisorGoals(firmId);

        Collection<Advisor> advisors = findAdvisorsOrdered(pageNum, firmId);

        if (advisors == null || advisors.isEmpty())
            return null;

        GoalResponse goalResponse = new GoalResponse();
        goalResponse.setTotalUsers(totalAdvisors);
        goalResponse.setTotalGoals(totalGoals);
        goalResponse.setUsers(advisors);
        goalResponse.setPage(pageNum);
        goalResponse.setCount(advisors.size());

        return goalResponse;
    }

}
