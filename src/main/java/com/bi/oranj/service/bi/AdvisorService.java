package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.Goal;
import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.GoalRepository;
import com.bi.oranj.model.bi.wrapper.user.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by jaloliddinbakirov on 5/24/17.
 * This service is responsible for advisor related logic
 */
@Service
public class AdvisorService extends GoalServiceAbstract {

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Value("${page.size}")
    private Integer pageSize;


    /**
     * Goal is nothing but JSON Template
     * returns ready Goal object to be send to the end user
     * Goal is nothing but JSON Template
     * returns Goal object
     * @param pageNum
     * @param firmId
     * @return
     */
    @Override
    public Goal buildResponse(int pageNum, long firmId) {
        int totalAdvisors = advisorRepository.findDistinctByFirm(firmId);
        int totalPages = totalPages(totalAdvisors);
        if (pageNum > totalPages) return null;

        Collection<Advisor> advisors = findGoals(firmId, pageNum);
        if (advisors == null || advisors.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")));
        int totalGoals = goalRepository.totalAdvisorGoals(firmId);
        Goal goals = processGoalresponse(advisors, pageNum, totalAdvisors, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        return goals;
    }

    /**
     * Goal is nothing but JSON Template
     * returns Goal object
     * for request with start date
     * @param startDate
     * @param pageNum
     * @param firmId
     * @return
     */
    @Override
    public Goal buildResponseWithStartDate (String startDate, int pageNum, long firmId){
        int totalAdvisors = advisorRepository.findDistinctAdvisorsWithStartDate(startDate, firmId);
        int totalPages = totalPages(totalAdvisors);
        if (pageNum > totalPages) return null;

        Collection<Advisor> advisors = findGoalsWithStartDate(firmId, startDate, pageNum);
        if (advisors == null || advisors.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")));
        int totalGoals = goalRepository.totalAdvisorGoalsWithStartDate(startDate, firmId);
        Goal goals = processGoalresponse(advisors, pageNum, totalAdvisors, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        return goals;
    }


    /**
     * Goal is nothing but JSON Template
     * returns Goal object
     * for request with end date
     * @param endDate
     * @param pageNum
     * @param firmId
     * @return
     */
    @Override
    public Goal buildResponseWithEndDate (String endDate, int pageNum, long firmId){
        int totalAdvisors = advisorRepository.findDistinctAdvisorsWithEndDate(endDate, firmId);
        int totalPages = totalPages(totalAdvisors);
        if (pageNum > totalPages) return null;

        Collection<Advisor> advisors = findGoalsWithEndDate(firmId, endDate, pageNum);
        if (advisors == null || advisors.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")));
        int totalGoals = goalRepository.totalAdvisorGoalsWithEndDate(endDate, firmId);
        Goal goals = processGoalresponse(advisors, pageNum, totalAdvisors, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        return goals;
    }

    /**
     * Goal is nothing but JSON Template
     * returns Goal object
     * for request with start and date date
     * @param startDate
     * @param endDate
     * @param pageNum
     * @param firmId
     * @return
     */
    @Override
    public Goal buildResponseByDateBetween (String startDate, String endDate, int pageNum, long firmId){
        int totalAdvisors = advisorRepository.findDistinctAdvisorsByDateBetween(startDate, endDate, firmId);
        int totalPages = totalPages(totalAdvisors);
        if (pageNum > totalPages) return null;

        Collection<Advisor> advisors = findGoalsByDate(firmId, startDate, endDate, pageNum);
        if (advisors == null || advisors.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")));
        int totalGoals = goalRepository.totalAdvisorGoalsByDateBetween(startDate, endDate, firmId);
        Goal goals = processGoalresponse(advisors, pageNum, totalAdvisors, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        return goals;
    }

    /**
     * returns appropriate data from repository
     *
     * @param firmId
     * @param pageNum
     * @return
     */
    private Collection<Advisor> findGoals (long firmId, int pageNum){
        List<Object[]> goalObjects = advisorRepository.findGoalsOrdered(firmId, pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
    }

    /**
     * returns appropriate data from repository
     * @param firmId
     * @param startDate
     * @param endDate
     * @param pageNum
     * @return
     */
    private Collection<Advisor> findGoalsByDate (long firmId, String startDate, String endDate, int pageNum){
        List<Object[]> goalObjects = advisorRepository.findGoalsByDateBetween(firmId, startDate, endDate, pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
    }

    /**
     * returns appropriate data from repository
     * @param firmId
     * @param startDate
     * @param pageNum
     * @return
     */
    private Collection<Advisor> findGoalsWithStartDate (long firmId, String startDate, int pageNum){
        List<Object[]> goalObjects = advisorRepository.findGoalsWithStartDate(firmId, startDate, pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
    }

    /**
     * returns appropriate data from repository
     * @param firmId
     * @param endDate
     * @param pageNum
     * @return
     */
    private Collection<Advisor> findGoalsWithEndDate (long firmId, String endDate, int pageNum){
        List<Object[]> goalObjects = advisorRepository.findGoalsWithEndDate(firmId, endDate, pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
    }


    /**
     * builds Goal by putting missing parameters for final JSON
     * @param advisors
     * @param pageNum
     * @param totalAdvisors
     * @param totalGoals
     * @return
     */
    private Goal processGoalresponse (Collection<Advisor> advisors, int pageNum, int totalAdvisors, int totalGoals){
        if (advisors == null || advisors.isEmpty())
            return null;

        Goal goal = new Goal();
        goal.setTotalUsers(totalAdvisors);
        goal.setTotalGoals(totalGoals);
        goal.setUsers(advisors);
        goal.setPage(pageNum);
        goal.setCount(advisors.size());

        return goal;
    }

    /**
     * maps data returned from database to Advisor object fields
     * @param goalObjects
     * @return
     */
    private Collection<Advisor> processObjectArrays (List<Object[]> goalObjects){
        Map<Integer, Advisor> linkedHashMap = new LinkedHashMap<>();
        StringBuilder concatenatedName = new StringBuilder();
        for (Object[] goal : goalObjects){
            concatenatedName.setLength(0);

            int advisorId = ((BigInteger) goal[0]).intValue();
            String firstName = (String) goal[1];
            String lastName = (String) goal[2];
            concatenatedName.append(firstName).append(" ").append(lastName);
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
                    linkedHashMap.put(advisorId, new Advisor(advisorId, concatenatedName.toString(), Collections.emptyMap(), count));
                    continue;
                }
                HashMap<String, Integer> goalList = new HashMap<>();
                goalList.put(type, count);

                linkedHashMap.put(advisorId, new Advisor(advisorId, concatenatedName.toString(), goalList, count));
            }
        }
        return linkedHashMap.values();
    }

    private int totalPages (int totalAdvisors){
        return (int) Math.ceil(totalAdvisors * 1d / pageSize) - 1;
    }
}
