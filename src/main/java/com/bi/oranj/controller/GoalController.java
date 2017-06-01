package com.bi.oranj.controller;

//import com.bi.oranj.dao.GoalDAO;
import com.bi.oranj.entity.GoalEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jaloliddinbakirov on 5/24/17.
 */
@Controller
public class GoalController {

//    @Autowired
//    private GoalDAO goalDao;


    @RequestMapping("/create")
    @ResponseBody
    public String create(String name) {
        String userId = "";
        try {
            GoalEntity goalEntity = new GoalEntity(name);
//            goalDao.save(goalEntity);
            userId = String.valueOf(goalEntity.getId());
        }
        catch (Exception ex) {
            return "Error creating the goal: " + ex.toString();
        }
        return "GoalEntity succesfully created with id = " + userId;
    }

    /**
     * GET /delete  --> Delete the goal having the passed id.
     */
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(long id) {
        try {
            GoalEntity goalEntity = new GoalEntity(id);
//            goalDao.delete(goalEntity);
        }
        catch (Exception ex) {
            return "Error deleting the goal:" + ex.toString();
        }
        return "GoalEntity succesfully deleted!";
    }

    /**
     * GET /get-by-name  --> Return the id for the goal having the passed
     * name.
     */
    @RequestMapping("/get-by-name")
    @ResponseBody
    public String getByName(String name) {
        String goalId = "";
        try {
//            GoalEntity goalEntity = goalDao.findByName(name);
//            goalId = String.valueOf(goalEntity.getId());
        }
        catch (Exception ex) {
            return "GoalEntity not found";
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
//            GoalEntity goalEntity = goalDao.findOne(id);
//            goalEntity.setName(name);
//            goalDao.save(goalEntity);
        }
        catch (Exception ex) {
            return "Error updating the goal: " + ex.toString();
        }
        return "GoalEntity successfully updated!";
    }

}
