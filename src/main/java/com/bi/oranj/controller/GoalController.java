package com.bi.oranj.controller;

//import com.bi.oranj.dao.GoalDAO;
import com.bi.oranj.controller.resp.BIResponse;
import com.bi.oranj.controller.resp.RestResponse;
import com.bi.oranj.entity.GoalEntity;
import com.bi.oranj.json.GoalResponse;
import com.bi.oranj.service.AdvisorService;
import com.bi.oranj.service.ClientService;
import com.bi.oranj.service.FirmService;
import com.bi.oranj.service.GoalService;
import com.bi.oranj.wrapper.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by jaloliddinbakirov on 5/24/17.
 */
@RestController
@RequestMapping("/goals")
public class GoalController {

    private final Logger logger = LoggerFactory.getLogger(GoalController.class);

    @Autowired
    FirmService firmService;

    @Autowired
    AdvisorService advisorService;

    @Autowired
    ClientService clientService;

    @RequestMapping (value = "/{userType}", method = RequestMethod.GET)
    public BIResponse getGoals (@PathVariable String userType, @RequestParam (value = "advisorId", required = false) Long advisorId,
                                @RequestParam (value = "firmId", required = false) Long firmId,
                                @RequestParam (value = "page") int pageNum, HttpServletResponse response) throws IOException {

        GoalService goalService = getService(userType);

        if (goalService != null){
            if (advisorId == null) advisorId = Long.valueOf(0);
            if (firmId == null) firmId = Long.valueOf(0);

            int totalPages = goalService.totalPages(firmId, advisorId);

            if (pageNum < 0){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return RestResponse.error("Bad input parameter");
            }
            if (pageNum > totalPages){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return RestResponse.success("Data not found");
            }

            GoalResponse goals;
            Collection <? extends User> users = goalService.findGoals(pageNum, firmId, advisorId);
            try{
                goals = goalService.buildResponse(pageNum, firmId, advisorId, users);
                if (pageNum == totalPages){
                    goals.setLast(true);
                }
            }catch (Exception ex){
                logger.error("Error while building response for firms: " + ex);
                ex.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return null;
            }

            return goals;
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return RestResponse.error("Bad input parameter");
    }


    public GoalService getService (String userType){
        switch (userType.toLowerCase()){
            case "firms":
                return firmService;
            case "advisors":
                return advisorService;
            case "clients":
                return clientService;
        }
        return null;
    }


}
