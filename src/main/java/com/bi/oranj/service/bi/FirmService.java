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
public class FirmService extends GoalServiceAbstract {
    @Autowired
    private FirmRepository firmRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Value("${page.size}")
    private Integer pageSize;


    @Override
    public Goal buildResponse(int pageNum, long userId) {
        int totalFirms = firmRepository.findDistinctFromFirm();
        int totalPages = totalPages(totalFirms);
        if (pageNum > totalPages)
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")), true);

        Collection<Firm> firms = findGoals(pageNum);
        if (firms == null || firms.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")), true);
        int totalGoals = goalRepository.totalGoals();
        Goal goals = processGoalresponse(firms, pageNum, totalFirms, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        return goals;
    }

    @Override
    public Goal buildResponseWithStartDate (String startDate, int pageNum, long userId){
        int totalFirms = firmRepository.findDistinctFirmsWithStartDate(startDate);
        int totalPages = totalPages(totalFirms);
        if (pageNum > totalPages)
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")), true);

        Collection<Firm> firms = findGoalsWithStartDate(startDate, pageNum);
        if (firms == null || firms.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")), true);
        int totalGoals = goalRepository.totalGoalsWithStartDate(startDate);
        Goal goals = processGoalresponse(firms, pageNum, totalFirms, totalGoals);
        if ( goals!=null && pageNum == totalPages) goals.setLast(true);
        return goals;
    }

    @Override
    public Goal buildResponseWithEndDate (String endDate, int pageNum, long userId){
        int totalFirms = firmRepository.findDistinctFirmsWithEndDate(endDate);
        int totalPages = totalPages(totalFirms);
        if (pageNum > totalPages)
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")), true);

        Collection<Firm> firms = findGoalsWithEndDate(endDate, pageNum);
        if (firms == null || firms.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")), true);
        int totalGoals = goalRepository.totalGoalsWithEndDate(endDate);
        Goal goals = processGoalresponse(firms, pageNum, totalFirms, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        return goals;
    }

    @Override
    public Goal buildResponseByDateBetween (String startDate, String endDate, int pageNum, long userId){
        int totalFirms = firmRepository.findDistinctFirmsByDateBetween(startDate, endDate);
        int totalPages = totalPages(totalFirms);
        if (pageNum > totalPages)
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")), true);

        Collection<Firm> firms = findGoalsByDateBetween(startDate, endDate, pageNum);
        if (firms == null || firms.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")), true);
        int totalGoals = goalRepository.totalGoalsByDateBetween(startDate, endDate);
        Goal goals = processGoalresponse(firms, pageNum, totalFirms, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        return goals;
    }

    private Collection<Firm> findGoals (int pageNum){
        List<Object[]> goalObjects = firmRepository.findGoalsOrdered(pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
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
        Map<Integer, Firm> idMapFirm = new LinkedHashMap<>();

        for (Object[] goal : goalObjects){
            int firmId = ((BigInteger) goal[0]).intValue();
            String firmName = (String) goal[1];
            int count = ((BigInteger) goal[3]).intValue();

            String type = "";
            if (goal[2] == null) type = null;
            else type = ((String) goal[2]).trim().toLowerCase();

            if (idMapFirm.containsKey(firmId)) {
                Firm firm = idMapFirm.get(firmId);
                HashMap<String, Integer> goalList = (HashMap<String, Integer>) firm.getGoals();

                if (goalList.containsKey(type)) {
                    goalList.put(type, goalList.get(type) + count);
                } else {
                    goalList.put(type, count);
                }
                firm.setGoals(goalList);
                firm.setTotal(count);
            } else {
                HashMap<String, Integer> goalList = new HashMap<>();
                goalList.put("custom", 0);
                goalList.put("college", 0);
                goalList.put("retirement", 0);
                goalList.put("insurance", 0);
                goalList.put("home", 0);
                goalList.put("special_event", 0);

                if (type == null) {
                    idMapFirm.put(firmId, new Firm(firmId, firmName, goalList, count));
                    continue;
                }
                goalList.put(type, count);
                idMapFirm.put(firmId, new Firm(firmId, firmName, goalList, count));
            }
        }

        return idMapFirm.values();
    }

    private Goal processGoalresponse (Collection<Firm> firms, int pageNum, int totalFirms, int totalGoals){
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
