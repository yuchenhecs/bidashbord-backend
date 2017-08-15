package com.bi.oranj.service.gamification;

import com.bi.oranj.model.gamification.Config;
import com.bi.oranj.repository.gamification.ConfigRepository;
import com.bi.oranj.utils.ApiResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jaloliddinbakirov on 7/20/17.
 */
@Service
public class ConfigService {

    @Autowired
    private ConfigRepository configRepository;

    public ResponseEntity<Object> getConfig(){
        List<Config> configs =  configRepository.findAll();
        return new ResponseEntity<>(configs, HttpStatus.OK);
    }

    public ResponseEntity<Object> getConfigByName(String configName){
        Config config = configRepository.findByConfigName(configName);
        if (config == null) return new ResponseEntity<>(new ApiResponseMessage("Config with the given config name not found"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(config, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateConfig(String configName, BigDecimal newValue){
        List<Config> configs = configRepository.findAll();
        boolean exists = false;
        for (Config config : configs){
            if (config.getConfigName().equalsIgnoreCase(configName)){
                exists = true;
                break;
            }
        }
        if (!exists) return new ResponseEntity<>(new ApiResponseMessage("Config with the given config name does not exist"), HttpStatus.BAD_REQUEST);

        configRepository.updateByConfigName(configName, newValue);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
