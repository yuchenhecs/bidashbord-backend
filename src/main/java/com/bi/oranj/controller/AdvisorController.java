package com.bi.oranj.controller;

import com.bi.oranj.controller.resp.BIResponse;
import com.bi.oranj.json.GoalResponse;
import com.bi.oranj.service.AdvisorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jaloliddinbakirov on 5/24/17.
 */
@RestController
@RequestMapping ("/goals")
public class AdvisorController {

    private final Logger logger = LoggerFactory.getLogger(AdvisorController.class);

    @Autowired
    private AdvisorService advisorService;

    @RequestMapping(value = "/advisor/{firmId}/page/{pageNum}", method = RequestMethod.GET)
    @ResponseBody
    public BIResponse getFirmsOrdered (@PathVariable int pageNum, @PathVariable long firmId){
        int totalPages = advisorService.totalPages(firmId);

        if (pageNum > totalPages){
            return RestResponse.success("Data not found");
        }

        GoalResponse advisors;
        try{
            advisors = advisorService.buildResponse(pageNum, firmId);
        }catch (Exception ex){
            logger.error("Error while building response for firms: " + ex);
            return null;
        }

        return advisors;
    }
}
