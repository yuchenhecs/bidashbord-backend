package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.GoalResponse;
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


    public int totalPages (long firmId){

        return (int) Math.ceil(advisorRepository.findDistinct(firmId).size() * 1d / pageSize);
    }

    public Collection<Advisor> findGoals (int pageNum, long firmId){

        List<Object[]> goalObjects = (List<Object[]>) advisorRepository.findGoalsOrdered(firmId, pageNum * pageSize, pageSize);

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

    public GoalResponse buildResponse (int pageNum, long firmId, Collection<Advisor> advisors){
        int totalAdvisors = advisorRepository.findDistinct(firmId).size();
        int totalGoals = goalRepository.totalAdvisorGoals(firmId);

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

    @Override
    public Collection<? extends User> findGoals(int pageNum, long firmId, long advisorId) {
        return findGoals(pageNum, firmId);
    }

    @Override
    public int totalPages(long firmId, long advisorId) {
        return totalPages(firmId);
    }

    @Override
    public GoalResponse buildResponse(int pageNum, long firmId, long advisorId, Collection<? extends User> users) {
        return buildResponse(pageNum, firmId, (Collection<Advisor>) users);
    }
}
