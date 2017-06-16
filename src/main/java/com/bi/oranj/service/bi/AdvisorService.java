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

import javax.servlet.http.HttpServletResponse;
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
    public GoalResponse buildResponse(int pageNum, long firmId, HttpServletResponse response) {
        int totalPages = totalPages(firmId);
        if (pageNum > totalPages) return null;

        Collection<Advisor> advisors = findGoals(firmId, pageNum);
        int totalAdvisors = advisorRepository.findDistinctByFirm(firmId);
        int totalGoals = goalRepository.totalAdvisorGoals(firmId);
        GoalResponse goals = processGoalresponse(advisors, pageNum, totalAdvisors, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    @Override
    public GoalResponse buildResponseWithStartDate (String startDate, int pageNum, long firmId, HttpServletResponse response){
        int totalPages = totalPagesWithStartDate(firmId, startDate);
        if (pageNum > totalPages) return null;

        Collection<Advisor> advisors = findGoalsWithStartDate(firmId, startDate, pageNum);
        int totalAdvisors = advisorRepository.findDistinctAdvisorsWithStartDate(startDate, firmId);
        int totalGoals = goalRepository.totalAdvisorGoalsWithStartDate(startDate, firmId);
        GoalResponse goals = processGoalresponse(advisors, pageNum, totalAdvisors, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    @Override
    public GoalResponse buildResponseWithEndDate (String endDate, int pageNum, long firmId, HttpServletResponse response){
        int totalPages = totalPagesWithEndDate(firmId, endDate);
        if (pageNum > totalPages) return null;

        Collection<Advisor> advisors = findGoalsWithEndDate(firmId, endDate, pageNum);
        int totalAdvisors = advisorRepository.findDistinctAdvisorsWithEndDate(endDate, firmId);
        int totalGoals = goalRepository.totalAdvisorGoalsWithEndDate(endDate, firmId);
        GoalResponse goals = processGoalresponse(advisors, pageNum, totalAdvisors, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    @Override
    public GoalResponse buildResponseByDateBetween (String startDate, String endDate, int pageNum, long firmId, HttpServletResponse response){
        int totalPages = totalPagesByDateBetween(firmId, startDate, endDate);
        if (pageNum > totalPages) return null;

        Collection<Advisor> advisors = findGoalsByDate(firmId, startDate, endDate, pageNum);
        int totalAdvisors = advisorRepository.findDistinctAdvisorsByDateBetween(startDate, endDate, firmId);
        int totalGoals = goalRepository.totalAdvisorGoalsByDateBetween(startDate, endDate, firmId);
        GoalResponse goals = processGoalresponse(advisors, pageNum, totalAdvisors, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }


    private Collection<Advisor> findGoals (long firmId, int pageNum){
        List<Object[]> goalObjects = advisorRepository.findGoalsOrdered(firmId, pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
    }

    private Collection<Advisor> findGoalsByDate (long firmId, String startDate, String endDate, int pageNum){
        List<Object[]> goalObjects = advisorRepository.findGoalsByDateBetween(firmId, startDate, endDate, pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
    }

    private Collection<Advisor> findGoalsWithStartDate (long firmId, String startDate, int pageNum){
        List<Object[]> goalObjects = advisorRepository.findGoalsWithStartDate(firmId, startDate, pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
    }

    private Collection<Advisor> findGoalsWithEndDate (long firmId, String endDate, int pageNum){
        List<Object[]> goalObjects = advisorRepository.findGoalsWithEndDate(firmId, endDate, pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
    }

    private GoalResponse processGoalresponse (Collection<Advisor> advisors, int pageNum, int totalAdvisors, int totalGoals){
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

    private Collection<Advisor> processObjectArrays (List<Object[]> goalObjects){
        Map<Integer, Advisor> linkedHashMap = new LinkedHashMap<>();

        for (Object[] goal : goalObjects){
            int advisorId = ((BigInteger) goal[0]).intValue();
            String firstName = (String) goal[1];
            String lastName = (String) goal[2];
            int count = ((BigInteger) goal[4]).intValue();

            String type = "";
            if (goal[3] == null) type = null;
            else type = ((String) goal[3]).trim().toLowerCase();


            if (linkedHashMap.containsKey(advisorId)){
                Advisor advisor = linkedHashMap.get(advisorId);

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
                    linkedHashMap.put(advisorId, new Advisor(advisorId, firstName, lastName, Collections.emptyMap(), count));
                    continue;
                }

                HashMap<String, Integer> goalList = new HashMap<>();
                goalList.put(type, count);

                linkedHashMap.put(advisorId, new Advisor(advisorId, firstName, lastName, goalList, count));
            }
        }


        return linkedHashMap.values();
    }


    @Override
    public int totalPagesWithStartDate (long firmId, String startDate) {
        return (int) Math.ceil(advisorRepository.findDistinctAdvisorsWithStartDate(startDate, firmId) * 1d / pageSize) - 1;
    }

    @Override
    public int totalPagesWithEndDate (long firmId, String endDate) {
        return (int) Math.ceil(advisorRepository.findDistinctAdvisorsWithEndDate(endDate, firmId) * 1d / pageSize) - 1;
    }

    @Override
    public int totalPagesByDateBetween (long firmId, String startDate, String endDate) {
        return (int) Math.ceil(advisorRepository.findDistinctAdvisorsByDateBetween(startDate, endDate, firmId) * 1d / pageSize) - 1;
    }


}
