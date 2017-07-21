package com.bi.oranj.service.gamification;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.gamification.Config;
import com.bi.oranj.repository.gamification.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jaloliddinbakirov on 7/20/17.
 */
@Service
public class ConfigService {

    @Autowired
    private ConfigRepository configRepository;

    public RestResponse getConfig(){
        List<Config> configs =  configRepository.findAll();
        return RestResponse.successWithoutMessage(configs);
    }

    public RestResponse getConfigByName(String configName){
        Config config = configRepository.findByConfigName(configName);
        if (config == null) return RestResponse.error("Config with the given config name not found");
        return RestResponse.successWithoutMessage(config);
    }

    public RestResponse updateConfig(String configName, BigDecimal newValue){
        List<Config> configs = configRepository.findAll();
        boolean exists = false;
        for (Config config : configs){
            if (config.getConfigName().equalsIgnoreCase(configName)){
                exists = true;
                break;
            }
        }
        if (!exists) return RestResponse.error("Config with the given config name does not exist");

        configRepository.updateByConfigName(configName, newValue);
        return RestResponse.success();
    }


}
