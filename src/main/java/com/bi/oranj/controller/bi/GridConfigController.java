package com.bi.oranj.controller.bi;

import com.bi.oranj.model.bi.Grid;
import com.bi.oranj.service.bi.GridConfigService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping (method = RequestMethod.GET, value = "/{userId}")
    public ResponseEntity getGridConfig (@PathVariable Long userId){
        return gridConfigService.getGridConfig(userId);
    }

    @ApiOperation(value = "Insert or update config by user id")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping (method = RequestMethod.POST)
    public ResponseEntity insertOrUpdateConfig (@RequestBody Map<String, Grid> gridContainer){
        return gridConfigService.insertOrUpdateIfExits(gridContainer);
    }
}
