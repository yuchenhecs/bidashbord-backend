package com.bi.oranj.controller.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.GridContainer;
import com.bi.oranj.model.bi.GridEntity;
import com.bi.oranj.service.bi.GridConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jaloliddinbakirov on 8/3/17.
 */

@RestController
@CrossOrigin
@RequestMapping("bi/grid-config")
public class GridConfigController {

    @Autowired
    private GridConfigService gridConfigService;

    @ApiOperation(value = "Get Grid Config by user id")
    @RequestMapping (method = RequestMethod.GET, value = "/{userId}")
    public ResponseEntity getGridConfig (@PathVariable Long userId){
        return gridConfigService.getGridConfig(userId);
    }

    @ApiOperation(value = "Insert or update config by user id")
    @RequestMapping (method = RequestMethod.POST)
    public ResponseEntity insertOrUpdateConfig (@RequestBody GridContainer gridContainer){
        return gridConfigService.insertOrUpdateIfExits(gridContainer);
    }
}
