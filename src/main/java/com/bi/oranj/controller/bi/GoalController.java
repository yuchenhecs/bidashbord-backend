package com.bi.oranj.controller.bi;

import com.bi.oranj.controller.bi.resp.BIResponse;
import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.GoalResponse;
import com.bi.oranj.service.bi.AdvisorService;
import com.bi.oranj.service.bi.ClientService;
import com.bi.oranj.service.bi.FirmService;
import com.bi.oranj.service.bi.GoalService;
import com.bi.oranj.model.bi.wrapper.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by jaloliddinbakirov on 5/24/17.
 */
@RestController
@CrossOrigin
@RequestMapping("/goals")
public class GoalController {

    private final Logger logger = LoggerFactory.getLogger(GoalController.class);

    @Autowired
    FirmService firmService;

    @Autowired
    AdvisorService advisorService;

    @Autowired
    ClientService clientService;

    @RequestMapping (value = "/firms", method = RequestMethod.GET)
    public BIResponse getFirmGoals (@RequestParam (value = "page", required = false) Integer pageNum, HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        return processRequest("firms", null, pageNum, request, response);
    }

    @RequestMapping (value = "/advisors", method = RequestMethod.GET)
    public BIResponse getAdvisorGoals (@RequestParam (value = "page", required = false) Integer pageNum, HttpServletRequest request,
                                HttpServletResponse response, @RequestParam (value = "firmId", required = true) Long firmId) throws IOException {
        return processRequest("advisors", firmId, pageNum, request, response);
    }

    @RequestMapping (value = "/clients", method = RequestMethod.GET)
    public BIResponse getClientGoals (@RequestParam (value = "page", required = false) Integer pageNum, HttpServletRequest request,
                                HttpServletResponse response, @RequestParam (value = "advisorId", required = true) Long advisorId) throws IOException {
        return processRequest("clients", advisorId, pageNum, request, response);
    }


    private BIResponse processRequest (String userType, Long userId, Integer pageNum, HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {
        GoalService goalService = getService(userType);

        if (goalService != null){
//            if (advisorId == null) advisorId = Long.valueOf(0);
//            if (firmId == null) firmId = Long.valueOf(0);

            if (userId == null) userId = Long.valueOf(0);
            if (pageNum == null) pageNum = Integer.valueOf(0);

            int totalPages = goalService.totalPages(userId);

            if (pageNum < 0){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return RestResponse.error("Bad input parameter");
            }
            if (pageNum > totalPages){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return RestResponse.error("Data not found");
            }

            GoalResponse goals;
            Collection <? extends User> users = goalService.findGoals(pageNum, userId);
            try{
                goals = goalService.buildResponse(pageNum, userId, users);
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


    private GoalService getService (String userType){
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
