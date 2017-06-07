package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.GoalResponse;
import com.bi.oranj.model.bi.wrapper.user.Client;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.GoalRepository;
import com.bi.oranj.model.bi.wrapper.User;
import com.bi.oranj.model.bi.wrapper.user.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by jaloliddinbakirov on 5/24/17.
 */
@Service
public class AdvisorService implements GoalService{

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Value("${page.size}")
    private Integer pageSize;

    @Override
    public int totalPages (long firmId){

        return (int) Math.ceil(advisorRepository.findDistinct(firmId).size() * 1d / pageSize) - 1;
    }

    @Override
    public Collection<Advisor> findGoals (int pageNum, long firmId){

        List<Object[]> goalObjects = (List<Object[]>) advisorRepository.findGoalsOrdered(firmId, pageNum * pageSize, pageSize);

        Map<Integer, Advisor> hashMap = new HashMap<>();

        for (Object[] goal : goalObjects){
            int advisorId = ((BigInteger) goal[0]).intValue();
            String firstName = (String) goal[1];
            String lastName = (String) goal[2];
            String type = ((String) goal[3]).trim().toLowerCase();
            int count = ((BigInteger) goal[4]).intValue();

            if (hashMap.containsKey(advisorId)){
                Advisor advisor = hashMap.get(advisorId);

                HashMap<String, Integer> goalList = advisor.getGoals();

                if (goalList.containsKey(type)){
                    goalList.put(type, goalList.get(type) + count);
                }else {
                    goalList.put(type, count);
                }
                advisor.setGoals(goalList);
                advisor.setTotal(count);

            } else {
                HashMap<String, Integer> goalList = new HashMap<>();
                if (goalList.containsKey(type)){
                    goalList.put(type, goalList.get(type) + count);
                }else {
                    goalList.put(type, count);
                }
                hashMap.put(advisorId, new Advisor(advisorId, firstName, lastName, goalList, count));
            }
        }


        return hashMap.values();
    }

    @Override
    public GoalResponse buildResponse (int pageNum, long firmId, Collection<? extends User> users){
        int totalAdvisors = advisorRepository.findDistinct(firmId).size();
        int totalGoals = goalRepository.totalAdvisorGoals(firmId);

        Collection<Advisor> clients = (Collection<Advisor>) users;

        if (clients == null || clients.isEmpty())
            return null;

        GoalResponse goalResponse = new GoalResponse();
        goalResponse.setTotalUsers(totalAdvisors);
        goalResponse.setTotalGoals(totalGoals);
        goalResponse.setUsers(clients);
        goalResponse.setPage(pageNum);
        goalResponse.setCount(clients.size());

        return goalResponse;
    }

}
