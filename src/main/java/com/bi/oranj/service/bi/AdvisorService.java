package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.GoalResponse;
import com.bi.oranj.model.bi.wrapper.user.Client;
import com.bi.oranj.model.bi.wrapper.user.Firm;
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
public class AdvisorService extends GoalService{

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Value("${page.size}")
    private Integer pageSize;

    @Override
    public int totalPages (long firmId){
        return (int) Math.ceil(advisorRepository.findDistinctByFirm(firmId) * 1d / pageSize) - 1;
    }

    @Override
    public GoalResponse buildResponse(int pageNum, long firmId) {
        Collection<Advisor> advisors = findGoals(firmId, pageNum);
        int totalAdvisors = advisorRepository.findDistinctByFirm(firmId);
        int totalGoals = goalRepository.totalAdvisorGoals(firmId);
        return processGoalresponse(advisors, firmId, pageNum, totalAdvisors, totalGoals);
    }

    @Override
    public GoalResponse buildResponseWithStartDate (String startDate, int pageNum, long firmId){
        Collection<Advisor> advisors = findGoalsWithStartDate(firmId, startDate, pageNum);
        int totalAdvisors = advisorRepository.findDistinctAdvisorsWithStartDate(startDate, firmId);
        int totalGoals = goalRepository.totalAdvisorGoalsWithStartDate(startDate, firmId);
        return processGoalresponse(advisors,firmId, pageNum, totalAdvisors, totalGoals);
    }

    @Override
    public GoalResponse buildResponseWithEndDate (String endDate, int pageNum, long firmId){
        Collection<Advisor> advisors = findGoalsWithEndDate(firmId, endDate, pageNum);
        int totalAdvisors = advisorRepository.findDistinctAdvisorsWithEndDate(endDate, firmId);
        int totalGoals = goalRepository.totalAdvisorGoalsWithEndDate(endDate, firmId);
        return processGoalresponse(advisors, firmId, pageNum, totalAdvisors, totalGoals);
    }

    @Override
    public GoalResponse buildResponseWithDate (String startDate, String endDate, int pageNum, long firmId){
        Collection<Advisor> advisors = findGoalsByDate(firmId, startDate, endDate, pageNum);
        int totalAdvisors = advisorRepository.findDistinctAdvisorsByDateBetween(startDate, endDate, firmId);
        int totalGoals = goalRepository.totalAdvisorGoalsByDateBetween(startDate, endDate, firmId);
        return processGoalresponse(advisors, firmId, pageNum, totalAdvisors, totalGoals);
    }


    private Collection<Advisor> findGoals (long firmId, int pageNum){
        List<Object[]> goalObjects = advisorRepository.findGoalsOrdered(firmId, pageNum * pageSize, pageSize);
        return processResults(goalObjects);
    }

    private Collection<Advisor> findGoalsByDate (long firmId, String startDate, String endDate, int pageNum){
        List<Object[]> goalObjects = advisorRepository.findGoalsByDateBetween(firmId, startDate, endDate, pageNum * pageSize, pageSize);
        return processResults(goalObjects);
    }

    private Collection<Advisor> findGoalsWithStartDate (long firmId, String startDate, int pageNum){
        List<Object[]> goalObjects = advisorRepository.findGoalsWithStartDate(firmId, startDate, pageNum * pageSize, pageSize);
        return processResults(goalObjects);
    }

    private Collection<Advisor> findGoalsWithEndDate (long firmId, String endDate, int pageNum){
        List<Object[]> goalObjects = advisorRepository.findGoalsWithEndDate(firmId, endDate, pageNum * pageSize, pageSize);
        return processResults(goalObjects);
    }

    private GoalResponse processGoalresponse (Collection<Advisor> advisors, long firmId, int pageNum, int totalAdvisors, int totalGoals){
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

    private Collection<Advisor> processResults (List<Object[]> goalObjects){
        Map<Integer, Advisor> hashMap = new HashMap<>();

        for (Object[] goal : goalObjects){
            int advisorId = ((BigInteger) goal[0]).intValue();
            String firstName = (String) goal[1];
            String lastName = (String) goal[2];
            int count = ((BigInteger) goal[4]).intValue();

            String type = "";
            if (goal[3] == null) type = null;
            else type = ((String) goal[3]).trim().toLowerCase();


            if (hashMap.containsKey(advisorId)){
                Advisor advisor = hashMap.get(advisorId);

                HashMap<String, Integer> goalList = (HashMap<String, Integer>) advisor.getGoals();

                if (goalList.containsKey(type)){
                    goalList.put(type, goalList.get(type) + count);
                }else {
                    goalList.put(type, count);
                }
                advisor.setGoals(goalList);
                advisor.setTotal(count);

            } else {

                if (type == null){
                    hashMap.put(advisorId, new Advisor(advisorId, firstName, lastName, Collections.emptyMap(), count));
                    continue;
                }

                HashMap<String, Integer> goalList = new HashMap<>();
                goalList.put(type, count);

                hashMap.put(advisorId, new Advisor(advisorId, firstName, lastName, goalList, count));
            }
        }


        return hashMap.values();
    }

}
