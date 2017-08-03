package com.bi.oranj.controller.bi;

import com.bi.oranj.service.bi.AUMService;
import com.bi.oranj.service.bi.NetWorthService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/bi/networth")
public class NetWorthController {

    @Autowired
    private AUMService aumService;

    @Autowired
    private NetWorthService networthService;

    @ApiOperation(value = "Get net worth for Oranj Admin", notes = "returns net worth for Oranj Admin")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping(path="/firms", method = RequestMethod.GET)
    public ResponseEntity<Object> getNetWorthForAdmin(@RequestParam(value = "page", required = true) Integer pageNumber) {
        return networthService.getNetWorthForAdmin(pageNumber);
    }

    @ApiOperation(value = "Get net worth for firm", notes = "returns net worth for Firm")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping(path="/advisors", method = RequestMethod.GET)
    public ResponseEntity<Object> getNetWorthForFirm(@RequestParam (value = "firmId", required = true) Long firmId,
                                           @RequestParam (value = "page", required = true) Integer pageNumber) {
        return networthService.getNetWorthForFirm(firmId, pageNumber);
    }

    @ApiOperation(value = "Get net worth for advisor", notes = "returns net worth for advisor")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping(path="/clients", method = RequestMethod.GET)
    public ResponseEntity<Object> getNetWorthForAdvisor(@RequestParam (value = "advisorId", required = true) Long advisorId,
                                              @RequestParam (value = "page", required = true) Integer pageNumber) {
        return networthService.getNetWorthForAdvisor(advisorId, pageNumber);
    }

    @ApiOperation(value = "Get net worth for summary", notes = "returns net worth for summary")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> getNetWorthSummary() {
        return networthService.getNetWorthSummary();
    }
}
