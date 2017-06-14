package com.bi.oranj.service.bi;

import com.bi.oranj.model.bi.GoalResponse;
import com.bi.oranj.model.bi.wrapper.user.Advisor;
import com.bi.oranj.model.bi.wrapper.user.Firm;
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
public class ClientService implements GoalService{

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Value("${page.size}")
    private Integer pageSize;


    public int totalPages (long advisorId){
        return (int) Math.ceil(clientRepository.findDistinctByAdvisorFromClients(advisorId).size() * 1d / pageSize) - 1;
    }


    public Collection<Client> findGoals (int pageNum, long advisorId){

        List<Object[]> goalObjects = (List<Object[]>) clientRepository.findGoalsOrderedByAdvisor(advisorId, pageNum * pageSize, pageSize);

        Map<Integer, Client> hashMap = new HashMap<>();

        for (Object[] goal : goalObjects){
            int clientId = ((BigInteger) goal[0]).intValue();
            String firstName = (String) goal[1];
            String lastName = (String) goal[2];
            int count = ((BigInteger) goal[4]).intValue();

            String type = "";
            if (goal[3] == null) type = null;
            else type = ((String) goal[3]).trim().toLowerCase();

            if (hashMap.containsKey(clientId)){
                Client client = hashMap.get(clientId);

                HashMap<String, Integer> goalList = (HashMap<String, Integer>) client.getGoals();

                if (goalList.containsKey(type)){
                    goalList.put(type, goalList.get(type) + count);
                }else {
                    goalList.put(type, count);
                }
                client.setGoals(goalList);
                client.setTotal(count);

            } else {
                if (type == null){
                    hashMap.put(clientId, new Client(clientId, firstName, lastName, Collections.emptyMap(), count));
                    continue;
                }

                HashMap<String, Integer> goalList = new HashMap<>();
                if (goalList.containsKey(type)){
                    goalList.put(type, goalList.get(type) + count);
                }else {
                    goalList.put(type, count);
                }
                hashMap.put(clientId, new Client(clientId, firstName, lastName, goalList, count));
            }
        }


        return hashMap.values();
    }

    @Override
    public GoalResponse buildResponse (int pageNum, long advisorId, Collection<? extends User> users){
        int totalClients = clientRepository.findDistinctByAdvisorFromClients(advisorId).size();
        int totalGoals = goalRepository.totalClientGoals(advisorId);


        Collection<Client> clients = (Collection<Client>) users;

        if (clients == null || clients.isEmpty()) {
            return null;
        }

        GoalResponse goalResponse = new GoalResponse();
        goalResponse.setTotalUsers(totalClients);
        goalResponse.setTotalGoals(totalGoals);
        goalResponse.setUsers(clients);
        goalResponse.setPage(pageNum);
        goalResponse.setCount(clients.size());

        return goalResponse;
    }
}
