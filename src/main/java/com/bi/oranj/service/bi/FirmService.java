package com.bi.oranj.service.bi;


import com.bi.oranj.model.bi.GoalResponse;
import com.bi.oranj.repository.bi.FirmRepository;
import com.bi.oranj.repository.bi.GoalRepository;
import com.bi.oranj.model.bi.wrapper.User;
import com.bi.oranj.model.bi.wrapper.user.Firm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by jaloliddinbakirov on 5/25/17.
 */
@Service
public class FirmService implements GoalService{
    @Autowired
    private FirmRepository firmRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Value("${page.size}")
    private Integer pageSize;

    public int totalPages (){
        return (int) Math.ceil( firmRepository.findDistinct().size() * 1d / pageSize) - 1;
    }


    public Collection<Firm> findGoals (int pageNum){

        List<Object[]> goalObjects = (List<Object[]>) firmRepository.findGoalsOrdered(pageNum * pageSize, pageSize);

        Map<Integer, Firm> hashMap = new HashMap<>();


        for (Object[] goal : goalObjects){
            int firmId = ((BigInteger) goal[0]).intValue();
            String firmName = (String) goal[1];
            int count = ((BigInteger) goal[3]).intValue();

            String type = "";
            if (goal[2] == null) type = null;
            else type = ((String) goal[2]).trim().toLowerCase();

            if (hashMap.containsKey(firmId)) {
                Firm firm = hashMap.get(firmId);
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
                    hashMap.put(firmId, new Firm(firmId, firmName, Collections.emptyMap(), count));
                    continue;
                }

                HashMap<String, Integer> goalList = new HashMap<>();
                goalList.put(type, count);
                hashMap.put(firmId, new Firm(firmId, firmName, goalList, count));
            }
        }


        return hashMap.values();
    }


    public GoalResponse buildResponse (int pageNum, Collection<Firm> firms){
        int totalFirms = firmRepository.findDistinct().size();
        int totalGoals = goalRepository.totalGoals();

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

    @Override
    public Collection<? extends User> findGoals(int pageNum, long userId) {
        return findGoals(pageNum);
    }

    @Override
    public int totalPages(long userId) {
        return totalPages();
    }

    @Override
    public GoalResponse buildResponse(int pageNum, long userId, Collection<? extends User> users) {
        return buildResponse(pageNum, (Collection<Firm>) users);
    }
}
