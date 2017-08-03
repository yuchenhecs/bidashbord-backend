package com.bi.oranj.controller.bi;

import com.bi.oranj.service.bi.AUMService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/bi/aums")
public class AUMController {

    @Autowired
    private AUMService aumService;

    @ApiOperation(value = "Get AUMs for Oranj Admin", notes = "returns AUM for Oranj Admin, date should be in 'yyyy-MM-dd' format")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping(path="/firms", method = RequestMethod.GET)
    public ResponseEntity<Object> getAUMForAdmin(@RequestParam (value = "page", required = true) Integer pageNumber,
                                                 @RequestParam(value = "previousDate", required = true) String previousDate,
                                                 @RequestParam(value = "currentDate", required = true) String currentDate) {
        return aumService.getAUMForAdmin(pageNumber, previousDate, currentDate);
    }

    @ApiOperation(value = "Get AUMs for Firm", notes = "returns AUM for Firm")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping(path="/advisors", method = RequestMethod.GET)
    public ResponseEntity<Object> getAUMForFirm(@RequestParam (value = "firmId", required = true) Long firmId,
                                      @RequestParam (value = "page", required = true) Integer pageNumber,
                                      @RequestParam(value = "previousDate", required = true) String previousDate,
                                      @RequestParam(value = "currentDate", required = true) String currentDate) {
        return aumService.getAUMForFirm(firmId, previousDate, currentDate, pageNumber);
    }

    @ApiOperation(value = "Get AUMs for Advisor", notes = "returns AUM for Advisor")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping(path="/clients", method = RequestMethod.GET)
    public ResponseEntity<Object> getAUMForAdvisor(@RequestParam (value = "advisorId", required = true) Long advisorId,
                                         @RequestParam (value = "page", required = true) Integer pageNumber,
                                         @RequestParam(value = "previousDate", required = true) String previousDate,
                                         @RequestParam(value = "currentDate", required = true) String currentDate) {
        return aumService.getAUMForAdvisor(advisorId, previousDate, currentDate, pageNumber);
    }

    @ApiOperation(value = "Get AUMs for Summary Page", notes = "returns AUMs in Oranj Platform")
    @ApiImplicitParam(name = "authorization", value = "Bearer 'tokenId'", required = true, dataType = "String", paramType = "header")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> getAUMSummary() {
        return aumService.getAUMSummary();
    }
}
