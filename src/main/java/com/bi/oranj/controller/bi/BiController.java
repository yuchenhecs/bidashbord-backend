package com.bi.oranj.controller.bi;

import com.bi.oranj.model.bi.BiGoal;
import com.bi.oranj.service.bi.BiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by harshavardhanpatil on 5/24/17.
 */
@RestController
@RequestMapping(value = "/bi", produces = MediaType.APPLICATION_JSON_VALUE)
public class BiController {

    @Autowired
    BiService biService;

    @GetMapping(path="/goals")
    public @ResponseBody Iterable<BiGoal> getBiGoals() {
        return biService.getGoals();
    }
}
