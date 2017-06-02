package com.bi.oranj.service;

import com.bi.oranj.entity.BiGoal;
import com.bi.oranj.repository.AdvisorRepository;
import com.bi.oranj.repository.ClientRepository;
import com.bi.oranj.repository.FirmRepository;
import com.bi.oranj.repository.GoalRepository;
import com.bi.oranj.wrapper.User;
import com.bi.oranj.wrapper.user.Firm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
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
public class GoalService {
    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private FirmRepository firmRepository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Value("${page.size}")
    private Integer pageSize;

//    public Collection<? extends User> findGoalsOrderedByUser (int pageNum, long firmId, long advisorId){
//
//        if (firmId < 0 || advisorId < 0){
//            return findGoalsOrderedByFirms(pageNum);
//        }
//    }

    public Collection<Firm> findGoalsOrderedByFirms (int pageNum){

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

//    public Collection<Firm> findGoalsOrderedByFirms (int pageNum){
//
//        List<Object[]> goalObjects = (List<Object[]>) firmRepository.findGoalsOrdered(pageNum * pageSize, pageSize);
//
//        Map<Integer, Firm> hashMap = new HashMap<>();
//
//        for (Object[] goal : goalObjects){
//            int firmId = ((BigInteger) goal[0]).intValue();
//            String firmName = (String) goal[1];
//            String[] types = ((String) goal[2]).split(",");
//            int count = ((BigInteger) goal[3]).intValue();
//
//            if (hashMap.containsKey(firmId)){
//                Firm firm = hashMap.get(firmId);
//
//                HashMap<String, Integer> goalList = firm.getGoals();
//                for (String s : types){
//                    s = s.toLowerCase();
//                    if (goalList.containsKey(s)){
//                        goalList.put(s, goalList.get(s) + 1);
//                    }else {
//                        goalList.put(s, 1);
//                    }
//                }
//
//                firm.setGoals(goalList);
//            } else {
//                HashMap<String, Integer> goalList = new HashMap<>();
//                for (String s : types){
//                    s = s.toLowerCase();
//                    if (goalList.containsKey(s)){
//                        goalList.put(s, goalList.get(s) + 1);
//                    }else {
//                        goalList.put(s, 1);
//                    }
//                }
//                hashMap.put(firmId, new Firm(firmId, firmName, goalList, count));
//            }
//        }
//
//        return hashMap.values();
//    }
}
