package com.bi.oranj.controller.bi;

import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.service.bi.AUMService;
import com.bi.oranj.service.bi.NetWorthService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    @RequestMapping(path="/firms", method = RequestMethod.GET)
    public RestResponse getNetWorthForAdmin(@RequestParam(value = "page", required = true) Integer pageNumber) {
        return networthService.getNetWorthForAdmin(pageNumber);
    }

    @ApiOperation(value = "Get net worth for firm", notes = "returns net worth for Firm")
    @RequestMapping(path="/advisors", method = RequestMethod.GET)
    public RestResponse getNetWorthForFirm(@RequestParam (value = "firmId", required = true) Long firmId,
                                           @RequestParam (value = "page", required = true) Integer pageNumber) {
        return networthService.getNetWorthForFirm(firmId, pageNumber);
    }

    @ApiOperation(value = "Get net worth for advisor", notes = "returns net worth for advisor")
    @RequestMapping(path="/clients", method = RequestMethod.GET)
    public RestResponse getNetWorthForAdvisor(@RequestParam (value = "advisorId", required = true) Long advisorId,
                                              @RequestParam (value = "page", required = true) Integer pageNumber) {
        return networthService.getNetWorthForAdvisor(advisorId, pageNumber);
    }

    @ApiOperation(value = "Get net worth for summary", notes = "returns net worth for summary")
    @RequestMapping(method = RequestMethod.GET)
    public RestResponse getNetWorthSummary() {
        return networthService.getNetWorthSummary();
    }
}
