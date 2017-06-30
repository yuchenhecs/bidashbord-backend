package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.GoalResponse;
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
    public GoalResponse buildResponse(int pageNum, long userId, HttpServletResponse response) {
        int totalFirms = firmRepository.findDistinctFromFirm();
        int totalPages = totalPages(totalFirms);
        if (pageNum > totalPages) return null;

        Collection<Firm> firms = findGoals(pageNum);
        int totalGoals = goalRepository.totalGoals();
        GoalResponse goals = processGoalresponse(firms, pageNum, totalFirms, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    @Override
    public GoalResponse buildResponseWithStartDate (String startDate, int pageNum, long userId, HttpServletResponse response){
        int totalFirms = firmRepository.findDistinctFirmsWithStartDate(startDate);
        int totalPages = totalPages(totalFirms);
        if (pageNum > totalPages) return null;

        Collection<Firm> firms = findGoalsWithStartDate(startDate, pageNum);
        int totalGoals = goalRepository.totalGoalsWithStartDate(startDate);
        GoalResponse goals = processGoalresponse(firms, pageNum, totalFirms, totalGoals);
        if ( goals!=null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    @Override
    public GoalResponse buildResponseWithEndDate (String endDate, int pageNum, long userId, HttpServletResponse response){
        int totalFirms = firmRepository.findDistinctFirmsWithEndDate(endDate);
        int totalPages = totalPages(totalFirms);
        if (pageNum > totalPages) return null;

        Collection<Firm> firms = findGoalsWithEndDate(endDate, pageNum);
        int totalGoals = goalRepository.totalGoalsWithEndDate(endDate);
        GoalResponse goals = processGoalresponse(firms, pageNum, totalFirms, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    @Override
    public GoalResponse buildResponseByDateBetween (String startDate, String endDate, int pageNum, long userId, HttpServletResponse response){
        int totalFirms = firmRepository.findDistinctFirmsByDateBetween(startDate, endDate);
        int totalPages = totalPages(totalFirms);
        if (pageNum > totalPages) return null;

        Collection<Firm> firms = findGoalsByDateBetween(startDate, endDate, pageNum);
        int totalGoals = goalRepository.totalGoalsByDateBetween(startDate, endDate);
        GoalResponse goals = processGoalresponse(firms, pageNum, totalFirms, totalGoals);
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

    private GoalResponse processGoalresponse (Collection<Firm> firms, int pageNum, int totalFirms, int totalGoals){

        if (firms == null || firms.isEmpty())
            return null;

        GoalResponse goalResponse = new GoalResponse();
        goalResponse.setTotalUsers(totalFirms);
        goalResponse.setTotalGoals(totalGoals);
        goalResponse.setUsers(firms);
        goalResponse.setPage(pageNum);
        goalResponse.setCount(firms.size());

        return goalResponse;
    }


    public int totalPages (int totalFirms){
        return (int) Math.ceil( totalFirms * 1d / pageSize) - 1;
    }

    public int totalPages (){
        return (int) Math.ceil( firmRepository.findDistinctFromFirm() * 1d / pageSize) - 1;
    }

    public int totalPagesWithStartDate (long userId, String startDate) {
        return (int) Math.ceil(firmRepository.findDistinctFirmsWithStartDate(startDate) * 1d / pageSize) - 1;
    }

    public int totalPagesWithEndDate (long userId, String endDate) {
        return (int) Math.ceil(firmRepository.findDistinctFirmsWithEndDate(endDate) * 1d / pageSize) - 1;
    }

    public int totalPagesByDateBetween (long userId, String startDate, String endDate) {
        return (int) Math.ceil(firmRepository.findDistinctFirmsByDateBetween(startDate, endDate) * 1d / pageSize) - 1;
    }

}
