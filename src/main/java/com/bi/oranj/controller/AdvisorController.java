package com.bi.oranj.controller;

import com.bi.oranj.controller.resp.BIResponse;
import com.bi.oranj.json.GoalResponse;
import com.bi.oranj.service.AdvisorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jaloliddinbakirov on 5/24/17.
 */
@RestController
@RequestMapping ("/goals")
public class AdvisorController {

    private final Logger logger = LoggerFactory.getLogger(AdvisorController.class);

    @Autowired
    private AdvisorService advisorService;

    @RequestMapping(value = "/advisor/{firmId}", method = RequestMethod.GET)
    @ResponseBody
    public BIResponse getFirmsOrdered (@RequestParam (value = "page") int pageNum, @PathVariable long firmId, HttpServletResponse response) throws IOException {
        int totalPages = advisorService.totalPages(firmId);

        if (pageNum < 0 || firmId < 0){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Bad input parameter");
        }
        if (pageNum > totalPages){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return RestResponse.success("Data not found");
        }

        GoalResponse advisors;
        try{
            advisors = advisorService.buildResponse(pageNum, firmId);
        }catch (Exception ex){
            logger.error("Error while building response for firms: " + ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }

        return advisors;
    }
}
