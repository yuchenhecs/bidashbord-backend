package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.GoalResponse;
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
public class ClientService extends GoalService{

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Value("${page.size}")
    private Integer pageSize;


    @Override
    public GoalResponse buildResponse(int pageNum, long advisorId, HttpServletResponse response) {
        int totalClients = clientRepository.findDistinctByAdvisor(advisorId);
        int totalPages = totalPages(totalClients);
        if (pageNum > totalPages) return null;

        Collection<Client> clients = findGoals(advisorId, pageNum);
        int totalGoals = goalRepository.totalClientGoals(advisorId);
        GoalResponse goals = processGoalresponse(clients, pageNum, totalClients, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    @Override
    public GoalResponse buildResponseWithStartDate (String startDate, int pageNum, long advisorId, HttpServletResponse response){
        int totalClients = clientRepository.findDistinctClientsWithStartDate(startDate, advisorId);
        int totalPages = totalPages(totalClients);
        if (pageNum > totalPages) return null;

        Collection<Client> clients = findGoalsWithStartDate(advisorId, startDate, pageNum);
        int totalGoals = goalRepository.totalClientGoalsWithStartDate(startDate, advisorId);
        GoalResponse goals = processGoalresponse(clients, pageNum, totalClients, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    @Override
    public GoalResponse buildResponseWithEndDate (String endDate, int pageNum, long advisorId, HttpServletResponse response){
        int totalClients = clientRepository.findDistinctClientsWithEndDate(endDate, advisorId);
        int totalPages = totalPages(totalClients);
        if (pageNum > totalPages) return null;

        Collection<Client> clients = findGoalsWithEndDate(advisorId, endDate, pageNum);
        int totalGoals = goalRepository.totalClientGoalsWithEndDate(endDate, advisorId);
        GoalResponse goals = processGoalresponse(clients, pageNum, totalClients, totalGoals);
        if (goals != null && pageNum == totalPages) goals.setLast(true);
        response.setStatus(HttpServletResponse.SC_OK);
        return goals;
    }

    @Override
    public GoalResponse buildResponseByDateBetween (String startDate, String endDate, int pageNum, long advisorId, HttpServletResponse response){
        int totalClients = clientRepository.findDistinctClientsByDateBetween(startDate, endDate, advisorId);
        int totalPages = totalPages(totalClients);
        if (pageNum > totalPages) return null;

        Collection<Client> clients = findGoalsByDate(advisorId, startDate, endDate, pageNum);
        int totalGoals = goalRepository.totalClientGoalsByDateBetween(startDate, endDate, advisorId);
        GoalResponse goals = processGoalresponse(clients, pageNum, totalClients, totalGoals);
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

    private GoalResponse processGoalresponse (Collection<Client> clients, int pageNum, int totalClients, int totalGoals){

        if (clients == null || clients.isEmpty())
            return null;

        GoalResponse goalResponse = new GoalResponse();
        goalResponse.setTotalUsers(totalClients);
        goalResponse.setTotalGoals(totalGoals);
        goalResponse.setUsers(clients);
        goalResponse.setPage(pageNum);
        goalResponse.setCount(clients.size());

        return goalResponse;
    }

    private Collection<Client> processObjectArrays (List<Object[]> goalObjects){
        Map<Integer, Client> linkedHashMap = new LinkedHashMap<>();

        for (Object[] goal : goalObjects){
            int advisorId = ((BigInteger) goal[0]).intValue();
            String firstName = (String) goal[1];
            String lastName = (String) goal[2];
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
                    linkedHashMap.put(advisorId, new Client(advisorId, firstName, lastName, Collections.emptyMap(), count));
                    continue;
                }

                HashMap<String, Integer> goalList = new HashMap<>();
                goalList.put(type, count);

                linkedHashMap.put(advisorId, new Client(advisorId, firstName, lastName, goalList, count));
            }
        }


        return linkedHashMap.values();
    }

    public int totalPages (int totalClients){
        return (int) Math.ceil(totalClients * 1d / pageSize) - 1;
    }

}
