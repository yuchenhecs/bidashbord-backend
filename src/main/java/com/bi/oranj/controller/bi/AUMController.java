package com.bi.oranj.controller.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.service.bi.AUMService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * Created by harshavardhanpatil on 6/9/17.
 */
@Api(basePath = "/bi/aum", description = "Operations with BI DB", produces = "application/json")
@RestController
@CrossOrigin
@RequestMapping("/bi/aum")
public class AUMController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AUMService aumService;

    @ApiOperation(value = "Get AUMs for Oranj Admin", notes = "returns AUM for Oranj Admin, date should be in 'yyyy-MM-dd' format")
    @RequestMapping(path="/admin", method = RequestMethod.GET)
    public RestResponse getAUMForAdmin(@RequestParam (value = "page", required = false) Integer pageNumber,
                                       @RequestParam(value = "previousDate", required = true) String previousDate,
                                       @RequestParam(value = "currentDate", required = true) String currentDate) {
        return aumService.getAUMForAdmin(pageNumber, previousDate, currentDate);
    }

    @ApiOperation(value = "Get AUMs for Firm", notes = "returns AUM for Firm")
    @RequestMapping(path="/firms", method = RequestMethod.GET)
    public RestResponse getAUMForFirm(@RequestParam (value = "firmId", required = true) Long firmId,
                                      @RequestParam (value = "page", required = false) Integer pageNumber,
                                      @RequestParam(value = "previousDate", required = true) String previousDate,
                                      @RequestParam(value = "currentDate", required = true) String currentDate) {
        return aumService.getAUMForFirm(firmId, previousDate, currentDate, pageNumber);
    }

    @ApiOperation(value = "Get AUMs for Advisor", notes = "returns AUM for Advisor")
    @RequestMapping(path="/advisors", method = RequestMethod.GET)
    public RestResponse getAUMForAdvisor(@RequestParam (value = "advisorId", required = true) Long advisorId,
                                         @RequestParam (value = "page", required = false) Integer pageNumber,
                                         @RequestParam(value = "previousDate", required = true) String previousDate,
                                         @RequestParam(value = "currentDate", required = true) String currentDate) {
        return aumService.getAUMForAdvisor(advisorId, previousDate, currentDate, pageNumber);
    }
}
