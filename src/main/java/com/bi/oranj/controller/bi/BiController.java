package com.bi.oranj.controller.bi;

import com.bi.oranj.model.bi.BiGoal;
import com.bi.oranj.service.bi.BiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by harshavardhanpatil on 5/24/17.
 */
@Api(basePath = "/bi", description = "Operations with BI DB", produces = "application/json")
@RestController
@RequestMapping(value = "/bi", produces = MediaType.APPLICATION_JSON_VALUE)
public class BiController {

    @Autowired
    BiService biService;

    @ApiOperation(value = "Get BI Goals", notes = "returns you all the goals stored")
    @GetMapping(path="/goals")
    public @ResponseBody Iterable<BiGoal> getBiGoals() {
        return biService.getGoals();
    }
}
