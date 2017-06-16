package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.GoalResponse;
import com.bi.oranj.repository.bi.ClientRepository;
import com.bi.oranj.repository.bi.GoalRepository;
import com.bi.oranj.model.bi.wrapper.User;
import com.bi.oranj.model.bi.wrapper.user.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by jaloliddinbakirov on 5/30/17.
 */
@Service
public class ClientService extends GoalService{

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Value("${page.size}")
    private Integer pageSize;


    @Override
    public int totalPages (long clientId){
        return (int) Math.ceil(clientRepository.findDistinctByAdvisor(clientId) * 1d / pageSize) - 1;
    }


    @Override
    public GoalResponse buildResponse(int pageNum, long advisorId) {
        int totalClients = clientRepository.findDistinctByAdvisor(advisorId);
        int totalGoals = goalRepository.totalClientGoals(advisorId);

        Collection<Client> clients = findGoals(advisorId, pageNum);
        return processGoalresponse(clients, pageNum, totalClients, totalGoals);
    }

    @Override
    public GoalResponse buildResponseWithStartDate (String startDate, int pageNum, long advisorId){
        int totalClients = clientRepository.findDistinctAdvisorsWithStartDate(startDate, advisorId);
        int totalGoals = goalRepository.totalClientGoalsWithStartDate(startDate, advisorId);

        Collection<Client> clients = findGoalsWithStartDate(advisorId, startDate, pageNum);
        return processGoalresponse(clients, pageNum, totalClients, totalGoals);
    }

    @Override
    public GoalResponse buildResponseWithEndDate (String endDate, int pageNum, long advisorId){
        int totalClients = clientRepository.findDistinctAdvisorsWithEndDate(endDate, advisorId);
        int totalGoals = goalRepository.totalClientGoalsWithEndDate(endDate, advisorId);

        Collection<Client> clients = findGoalsWithEndDate(advisorId, endDate, pageNum);
        return processGoalresponse(clients, pageNum, totalClients, totalGoals);
    }

    @Override
    public GoalResponse buildResponseWithDate (String startDate, String endDate, int pageNum, long advisorId){
        int totalClients = clientRepository.findDistinctAdvisorsByDateBetween(startDate, endDate, advisorId);
        int totalGoals = goalRepository.totalClientGoalsByDateBetween(startDate, endDate, advisorId);

        Collection<Client> clients = findGoalsByDate(advisorId, startDate, endDate, pageNum);
        return processGoalresponse(clients, pageNum, totalClients, totalGoals);
    }


    private Collection<Client> findGoals (long advisorId, int pageNum){
        List<Object[]> goalObjects = clientRepository.findGoalsOrdered(advisorId, pageNum * pageSize, pageSize);
        return processResults(goalObjects);
    }

    private Collection<Client> findGoalsByDate (long advisorId, String startDate, String endDate, int pageNum){
        List<Object[]> goalObjects = clientRepository.findGoalsByDateBetween(advisorId, startDate, endDate, pageNum * pageSize, pageSize);
        return processResults(goalObjects);
    }

    private Collection<Client> findGoalsWithStartDate (long advisorId, String startDate, int pageNum){
        List<Object[]> goalObjects = clientRepository.findGoalsWithStartDate(advisorId, startDate, pageNum * pageSize, pageSize);
        return processResults(goalObjects);
    }

    private Collection<Client> findGoalsWithEndDate (long advisorId, String endDate, int pageNum){
        List<Object[]> goalObjects = clientRepository.findGoalsWithEndDate(advisorId, endDate, pageNum * pageSize, pageSize);
        return processResults(goalObjects);
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

    private Collection<Client> processResults (List<Object[]> goalObjects){
        Map<Integer, Client> hashMap = new HashMap<>();

        for (Object[] goal : goalObjects){
            int advisorId = ((BigInteger) goal[0]).intValue();
            String firstName = (String) goal[1];
            String lastName = (String) goal[2];
            int count = ((BigInteger) goal[4]).intValue();

            String type = "";
            if (goal[3] == null) type = null;
            else type = ((String) goal[3]).trim().toLowerCase();


            if (hashMap.containsKey(advisorId)){
                Client advisor = hashMap.get(advisorId);

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
                    hashMap.put(advisorId, new Client(advisorId, firstName, lastName, Collections.emptyMap(), count));
                    continue;
                }

                HashMap<String, Integer> goalList = new HashMap<>();
                goalList.put(type, count);

                hashMap.put(advisorId, new Client(advisorId, firstName, lastName, goalList, count));
            }
        }


        return hashMap.values();
    }

}
