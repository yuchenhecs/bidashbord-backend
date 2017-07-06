package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.Goal;
import com.bi.oranj.repository.bi.FirmRepository;
import com.bi.oranj.repository.bi.GoalRepository;
import com.bi.oranj.model.bi.wrapper.user.Firm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.*;

@Service
public class FirmService extends GoalService{
    @Autowired
    private FirmRepository firmRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Value("${page.size}")
    private Integer pageSize;


    @Override
    public Goal buildResponse(int pageNum, long userId, HttpServletResponse response) {
        int totalFirms = firmRepository.findDistinctFromFirm();
        int totalPages = totalPages(totalFirms);
        if (pageNum > totalPages) return null;

        Collection<Firm> firms = findGoals(pageNum);
        if (firms == null || firms.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")));
        int totalGoals = goalRepository.totalGoals();
        Goal goals = processGoalresponse(firms, pageNum, totalFirms, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    @Override
    public Goal buildResponseWithStartDate (String startDate, int pageNum, long userId, HttpServletResponse response){
        int totalFirms = firmRepository.findDistinctFirmsWithStartDate(startDate);
        int totalPages = totalPages(totalFirms);
        if (pageNum > totalPages) return null;

        Collection<Firm> firms = findGoalsWithStartDate(startDate, pageNum);
        if (firms == null || firms.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")));
        int totalGoals = goalRepository.totalGoalsWithStartDate(startDate);
        Goal goals = processGoalresponse(firms, pageNum, totalFirms, totalGoals);
        if ( goals!=null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    @Override
    public Goal buildResponseWithEndDate (String endDate, int pageNum, long userId, HttpServletResponse response){
        int totalFirms = firmRepository.findDistinctFirmsWithEndDate(endDate);
        int totalPages = totalPages(totalFirms);
        if (pageNum > totalPages) return null;

        Collection<Firm> firms = findGoalsWithEndDate(endDate, pageNum);
        if (firms == null || firms.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")));
        int totalGoals = goalRepository.totalGoalsWithEndDate(endDate);
        Goal goals = processGoalresponse(firms, pageNum, totalFirms, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    @Override
    public Goal buildResponseByDateBetween (String startDate, String endDate, int pageNum, long userId, HttpServletResponse response){
        int totalFirms = firmRepository.findDistinctFirmsByDateBetween(startDate, endDate);
        int totalPages = totalPages(totalFirms);
        if (pageNum > totalPages) return null;

        Collection<Firm> firms = findGoalsByDateBetween(startDate, endDate, pageNum);
        if (firms == null || firms.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")));
        int totalGoals = goalRepository.totalGoalsByDateBetween(startDate, endDate);
        Goal goals = processGoalresponse(firms, pageNum, totalFirms, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    private Collection<Firm> findGoals (int pageNum){
        List<Object[]> firms = firmRepository.findGoalsOrdered(pageNum * pageSize, pageSize);
        return processObjectArrays(firms);
    }

    private Collection<Firm> findGoalsByDateBetween (String startDate, String endDate, int pageNum){
        List<Object[]> goalObjects = firmRepository.findGoalsByDateBetween(startDate, endDate, pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
    }

    private Collection<Firm> findGoalsWithStartDate (String startDate, int pageNum){
        List<Object[]> goalObjects = firmRepository.findGoalsWithStartDate(startDate, pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
    }

    private Collection<Firm> findGoalsWithEndDate (String endDate, int pageNum){
        List<Object[]> goalObjects = firmRepository.findGoalsWithEndDate(endDate, pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
    }

    private Collection<Firm> processObjectArrays (List<Object[]> goalObjects){
        Map<Integer, Firm> linkedHashMap = new LinkedHashMap<>();

        for (Object[] goal : goalObjects){
            int firmId = ((BigInteger) goal[0]).intValue();
            String firmName = (String) goal[1];
            int count = ((BigInteger) goal[3]).intValue();

            String type = "";
            if (goal[2] == null) type = null;
            else type = ((String) goal[2]).trim().toLowerCase();

            if (linkedHashMap.containsKey(firmId)) {
                Firm firm = linkedHashMap.get(firmId);
                HashMap<String, Integer> goalList = (HashMap<String, Integer>) firm.getGoals();

                if (goalList.containsKey(type)) {
                    goalList.put(type, goalList.get(type) + count);
                } else {
                    goalList.put(type, count);
                }
                firm.setGoals(goalList);
                firm.setTotal(count);
            } else {

                if (type == null) {
                    linkedHashMap.put(firmId, new Firm(firmId, firmName, Collections.emptyMap(), count));
                    continue;
                }

                HashMap<String, Integer> goalList = new HashMap<>();
                goalList.put(type, count);
                linkedHashMap.put(firmId, new Firm(firmId, firmName, goalList, count));
            }
        }

        return linkedHashMap.values();
    }

    private Goal processGoalresponse (Collection<Firm> firms, int pageNum, int totalFirms, int totalGoals){

        if (firms == null || firms.isEmpty())
            return null;

        Goal goal = new Goal();
        goal.setTotalUsers(totalFirms);
        goal.setTotalGoals(totalGoals);
        goal.setUsers(firms);
        goal.setPage(pageNum);
        goal.setCount(firms.size());

        return goal;
    }

    private int totalPages (int totalFirms){
        return (int) Math.ceil( totalFirms * 1d / pageSize) - 1;
    }
}
