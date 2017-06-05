package com.bi.oranj.service;

import com.bi.oranj.json.GoalResponse;
import com.bi.oranj.repository.ClientRepository;
import com.bi.oranj.repository.GoalRepository;
import com.bi.oranj.wrapper.User;
import com.bi.oranj.wrapper.user.Advisor;
import com.bi.oranj.wrapper.user.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public int totalPages (long firmId, long advisorId){
        return (int) Math.ceil(clientRepository.findDistinctByFirmByAdvisor(firmId, advisorId).size() * 1d / pageSize) - 1;
    }


    public Collection<Client> findGoals (int pageNum, long firmId, long advisorId){

        List<Object[]> goalObjects = (List<Object[]>) clientRepository.findGoalsOrderedByFirmByAdvisor(firmId, advisorId, pageNum * pageSize, pageSize);

        Map<Integer, Client> hashMap = new HashMap<>();

        for (Object[] goal : goalObjects){
            int clientId = ((BigInteger) goal[0]).intValue();
            String firstName = (String) goal[1];
            String lastName = (String) goal[2];
            String[] types = ((String) goal[3]).split(",");
            int count = ((BigInteger) goal[4]).intValue();

            if (hashMap.containsKey(clientId)){
                Client client = hashMap.get(clientId);

                HashMap<String, Integer> goalList = client.getGoals();
                for (String s : types){
                    s = s.toLowerCase();
                    if (goalList.containsKey(s)){
                        goalList.put(s, goalList.get(s) + 1);
                    }else {
                        goalList.put(s, 1);
                    }
                }

                client.setGoals(goalList);
            } else {
                HashMap<String, Integer> goalList = new HashMap<>();
                for (String s : types){
                    s = s.toLowerCase();
                    if (goalList.containsKey(s)){
                        goalList.put(s, goalList.get(s) + 1);
                    }else {
                        goalList.put(s, 1);
                    }
                }
                hashMap.put(clientId, new Client(clientId, firstName, lastName, goalList, count));
            }
        }


        return hashMap.values();
    }

    @Override
    public GoalResponse buildResponse (int pageNum, long firmId, long advisorId, Collection<? extends User> users){
        int totalClients = clientRepository.findDistinctByFirmByAdvisor(firmId, advisorId).size();
        int totalGoals = goalRepository.totalClientGoals(firmId, advisorId);


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
