package com.bi.oranj.controller;

import com.bi.oranj.controller.resp.BIResponse;
import com.bi.oranj.entity.GoalEntity;
import com.bi.oranj.json.GoalResponse;
import com.bi.oranj.service.FirmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jaloliddinbakirov on 5/25/17.
 */
@RestController
@RequestMapping("/goals")
public class FirmController {

    private final Logger logger = LoggerFactory.getLogger(FirmController.class);

    @Autowired
    FirmService firmService;

    @RequestMapping(value = "/firm/page/{pageNum}", method = RequestMethod.GET)
    @ResponseBody
    public BIResponse getFirmsOrdered (@PathVariable int pageNum){
        int totalPages = firmService.totalPages();

        if (pageNum > totalPages){
            return RestResponse.success("Data not found");
        }

        GoalResponse firms;
        try{
            firms = firmService.buildResponse(pageNum);
        }catch (Exception ex){
            logger.error("Error while building response for firms: " + ex);
            return null;
        }
        return firms;
    }


//    @RequestMapping(value = "/firm/test")
//    @ResponseBody
//    public List<GoalEntity> getFirms (){
//        List<GoalEntity> firms;
//        try{
//            firms = firmService.findFirms();
//        }catch (Exception ex){
//            logger.error("Error while building response for firms: " + ex);
//            return null;
//        }
//        return firms;
//    }

}
