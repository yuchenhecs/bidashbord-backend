package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.Goal;
import com.bi.oranj.repository.bi.ClientRepository;
import com.bi.oranj.repository.bi.GoalRepository;
import com.bi.oranj.model.bi.wrapper.user.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.*;

@Service
public class ClientService extends GoalServiceAbstract {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Value("${page.size}")
    private Integer pageSize;


    @Override
    public Goal buildResponse(int pageNum, long advisorId, HttpServletResponse response) {
        int totalClients = clientRepository.findDistinctByAdvisor(advisorId);
        int totalPages = totalPages(totalClients);
        if (pageNum > totalPages) return null;

        Collection<Client> clients = findGoals(advisorId, pageNum);
        if (clients == null || clients.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")));
        int totalGoals = goalRepository.totalClientGoals(advisorId);
        Goal goals = processGoalresponse(clients, pageNum, totalClients, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    @Override
    public Goal buildResponseWithStartDate (String startDate, int pageNum, long advisorId, HttpServletResponse response){
        int totalClients = clientRepository.findDistinctClientsWithStartDate(startDate, advisorId);
        int totalPages = totalPages(totalClients);
        if (pageNum > totalPages) return null;

        Collection<Client> clients = findGoalsWithStartDate(advisorId, startDate, pageNum);
        if (clients == null || clients.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")));
        int totalGoals = goalRepository.totalClientGoalsWithStartDate(startDate, advisorId);
        Goal goals = processGoalresponse(clients, pageNum, totalClients, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    @Override
    public Goal buildResponseWithEndDate (String endDate, int pageNum, long advisorId, HttpServletResponse response){
        int totalClients = clientRepository.findDistinctClientsWithEndDate(endDate, advisorId);
        int totalPages = totalPages(totalClients);
        if (pageNum > totalPages) return null;

        Collection<Client> clients = findGoalsWithEndDate(advisorId, endDate, pageNum);
        if (clients == null || clients.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")));
        int totalGoals = goalRepository.totalClientGoalsWithEndDate(endDate, advisorId);
        Goal goals = processGoalresponse(clients, pageNum, totalClients, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    @Override
    public Goal buildResponseByDateBetween (String startDate, String endDate, int pageNum, long advisorId, HttpServletResponse response){
        int totalClients = clientRepository.findDistinctClientsByDateBetween(startDate, endDate, advisorId);
        int totalPages = totalPages(totalClients);
        if (pageNum > totalPages) return null;

        Collection<Client> clients = findGoalsByDate(advisorId, startDate, endDate, pageNum);
        if (clients == null || clients.isEmpty())
            return new Goal(Collections.emptyList(), this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf("S")));
        int totalGoals = goalRepository.totalClientGoalsByDateBetween(startDate, endDate, advisorId);
        Goal goals = processGoalresponse(clients, pageNum, totalClients, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }


    private Collection<Client> findGoals (long advisorId, int pageNum){
        List<Object[]> goalObjects = clientRepository.findGoalsOrdered(advisorId, pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
    }

    private Collection<Client> findGoalsByDate (long advisorId, String startDate, String endDate, int pageNum){
        List<Object[]> goalObjects = clientRepository.findGoalsByDateBetween(advisorId, startDate, endDate, pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
    }

    private Collection<Client> findGoalsWithStartDate (long advisorId, String startDate, int pageNum){
        List<Object[]> goalObjects = clientRepository.findGoalsWithStartDate(advisorId, startDate, pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
    }

    private Collection<Client> findGoalsWithEndDate (long advisorId, String endDate, int pageNum){
        List<Object[]> goalObjects = clientRepository.findGoalsWithEndDate(advisorId, endDate, pageNum * pageSize, pageSize);
        return processObjectArrays(goalObjects);
    }

    private Goal processGoalresponse (Collection<Client> clients, int pageNum, int totalClients, int totalGoals){

        Goal goal = new Goal();
        goal.setTotalUsers(totalClients);
        goal.setTotalGoals(totalGoals);
        goal.setUsers(clients);
        goal.setPage(pageNum);
        goal.setCount(clients.size());

        return goal;
    }

    private Collection<Client> processObjectArrays (List<Object[]> goalObjects){

        if (goalObjects == null || goalObjects.isEmpty())
            return Collections.emptyList();

        Map<Integer, Client> linkedHashMap = new LinkedHashMap<>();
        StringBuilder concatenatedName = new StringBuilder();

        for (Object[] goal : goalObjects){
            concatenatedName.setLength(0); // clears string builder

            int advisorId = ((BigInteger) goal[0]).intValue();
            String firstName = (String) goal[1];
            String lastName = (String) goal[2];
            concatenatedName.append(firstName).append(" ").append(lastName);
            int count = ((BigInteger) goal[4]).intValue();

            String type = "";
            if (goal[3] == null) type = null;
            else type = ((String) goal[3]).trim().toLowerCase();


            if (linkedHashMap.containsKey(advisorId)){
                Client advisor = linkedHashMap.get(advisorId);
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
                    linkedHashMap.put(advisorId, new Client(advisorId, concatenatedName.toString(), Collections.emptyMap(), count));
                    continue;
                }

                HashMap<String, Integer> goalList = new HashMap<>();
                goalList.put(type, count);

                linkedHashMap.put(advisorId, new Client(advisorId, concatenatedName.toString(), goalList, count));
            }
        }


        return linkedHashMap.values();
    }

    private int totalPages (int totalClients){
        return (int) Math.ceil(totalClients * 1d / pageSize) - 1;
    }

}
