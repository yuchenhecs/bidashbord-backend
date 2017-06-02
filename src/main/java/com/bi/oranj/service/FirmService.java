package com.bi.oranj.service;


import com.bi.oranj.entity.BiGoal;
import com.bi.oranj.entity.GoalEntity;
import com.bi.oranj.json.GoalResponse;
import com.bi.oranj.repository.FirmRepository;
import com.bi.oranj.repository.GoalRepository;
import com.bi.oranj.wrapper.user.Firm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by jaloliddinbakirov on 5/25/17.
 */
@Service
public class FirmService {
    @Autowired
    private FirmRepository firmRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Value("${page.size}")
    private Integer pageSize;

    public int totalPages (){
        return firmRepository.findDistinct().size() / pageSize;
    }

    public Collection<Firm> findFirmsOrdered (int pageNum){

        List<BiGoal> goalObjects = firmRepository.findGoalsOrdered(pageNum * pageSize, pageSize);

        Map<Long, Firm> hashMap = new HashMap<>();

        for (BiGoal goal : goalObjects){
            long firmId = goal.getFirmId();
            String firmName = goal.getFirmName();
            String[] types = goal.getType().split(",");
            int count = goal.getCount().intValue();

            if (hashMap.containsKey(firmId)){
                Firm firm = hashMap.get(firmId);

                HashMap<String, Integer> goalList = firm.getGoals();
                for (String s : types){
                    s = s.toLowerCase();
                    if (goalList.containsKey(s)){
                        goalList.put(s, goalList.get(s) + 1);
                    }else {
                        goalList.put(s, 1);
                    }
                }

                firm.setGoals(goalList);
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
                hashMap.put(firmId, new Firm(firmId, firmName, goalList, count));
            }
        }

        return hashMap.values();
    }

    public GoalResponse buildResponse (int pageNum){
        int totalFirms = firmRepository.findDistinct().size();
        int totalGoals = goalRepository.totalGoals();

        Collection<Firm> firms = findFirmsOrdered(pageNum);

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

}
