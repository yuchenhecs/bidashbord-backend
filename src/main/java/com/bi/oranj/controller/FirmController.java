package com.bi.oranj.controller;

import com.bi.oranj.controller.resp.BIResponse;
import com.bi.oranj.controller.resp.RestResponse;
import com.bi.oranj.json.GoalResponse;
import com.bi.oranj.service.FirmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jaloliddinbakirov on 5/25/17.
 */
@RestController
@RequestMapping("/goals")
public class FirmController {

    private final Logger logger = LoggerFactory.getLogger(FirmController.class);

    @Autowired
    FirmService firmService;

    @RequestMapping(value = "/firm", method = RequestMethod.GET)
    @ResponseBody
    public BIResponse getFirmsOrdered (@RequestParam (value = "page") int pageNum, HttpServletResponse response) throws IOException {
        int totalPages = firmService.totalPages();
        if (pageNum < 0){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return RestResponse.error("Bad input parameter");
        }
        if (pageNum > totalPages){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return RestResponse.success("Data not found");
        }

        GoalResponse firms;
        try{
            firms = firmService.buildResponse(pageNum);
            if (pageNum == totalPages){
                firms.setLast(true);
            }
        }catch (Exception ex){
            logger.error("Error while building response for firms: " + ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
        return firms;
    }


}
