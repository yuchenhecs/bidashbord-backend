package com.bi.oranj.controller;

import com.bi.oranj.dao.GoalDAO;
import com.bi.oranj.model.Goal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jaloliddinbakirov on 5/24/17.
 */
@Controller
public class GoalController {

    @Autowired
    private GoalDAO goalDao;


    @RequestMapping("/create")
    @ResponseBody
    public String create(String name) {
        String userId = "";
        try {
            Goal goal = new Goal(name);
            goalDao.save(goal);
            userId = String.valueOf(goal.getId());
        }
        catch (Exception ex) {
            return "Error creating the goal: " + ex.toString();
        }
        return "Goal succesfully created with id = " + userId;
    }

    /**
     * GET /delete  --> Delete the goal having the passed id.
     */
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(long id) {
        try {
            Goal goal = new Goal(id);
            goalDao.delete(goal);
        }
        catch (Exception ex) {
            return "Error deleting the goal:" + ex.toString();
        }
        return "Goal succesfully deleted!";
    }

    /**
     * GET /get-by-name  --> Return the id for the goal having the passed
     * name.
     */
    @RequestMapping("/getByName")
    @ResponseBody
    public String getByName(String name) {
        String goalId = "";
        try {
            Goal goal = goalDao.findByName(name);
            goalId = String.valueOf(goal.getId());
        }
        catch (Exception ex) {
            return "Goal not found";
        }
        return "The goal id is: " + goalId;
    }

    /**
     * GET /update  --> Update the name for the goal in the
     * database having the passed id.
     */
    @RequestMapping("/update")
    @ResponseBody
    public String updateGoal(long id, String name) {
        try {
            Goal goal = goalDao.findOne(id);
            goal.setName(name);
            goalDao.save(goal);
        }
        catch (Exception ex) {
            return "Error updating the goal: " + ex.toString();
        }
        return "Goal succesfully updated!";
    }

}
