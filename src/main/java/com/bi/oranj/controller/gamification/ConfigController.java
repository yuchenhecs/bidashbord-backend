package com.bi.oranj.controller.gamification;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.service.gamification.ConfigService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Created by jaloliddinbakirov on 7/20/17.
 */
@RestController
@CrossOrigin
@RequestMapping("leaderboard/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;


    @ApiOperation(value = "Get list of configs")
    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public RestResponse getConfig (){
        return configService.getConfig();
    }

    @ApiOperation(value = "Get config by name")
    @RequestMapping(path = "/one", method = RequestMethod.GET)
    public RestResponse getConfigByName (@RequestParam ("configName") String configName){
        return configService.getConfigByName(configName);
    }

    @ApiOperation(value = "Update config by name")
    @RequestMapping(path = "/{configName}/{newValue}", method = RequestMethod.PUT)
    public RestResponse updateConfigByName (@PathVariable String configName, @PathVariable BigDecimal newValue){
        return configService.updateConfig(configName, newValue);
    }
}
