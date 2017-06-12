package com.bi.oranj.controller.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.service.bi.AUMService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "Get Goals created for given date", notes = "date should be in 'yyyy-MM-dd' format")
    @RequestMapping(path="/firms", method = RequestMethod.GET)
    public RestResponse getAUMForAdmin(@RequestParam (value = "page", required = false) Integer pageNumber) {
        return aumService.getAUMForAdmin(pageNumber);
    }
}
