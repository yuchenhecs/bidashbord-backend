package com.bi.oranj.controller;

import com.bi.oranj.controller.bi.AUMController;
import com.bi.oranj.controller.bi.resp.RestResponse;
import com.bi.oranj.model.bi.*;
import com.bi.oranj.service.bi.AUMService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = AUMController.class, secure = false)
public class AUMControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AUMService aumService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testGetAUMForAdmin() throws Exception{

        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(new FileReader("src/test/resources/json/AUMForAdmin.json"));
        String expected = data.toJSONString();

        Mockito.when(aumService.getAUMForAdmin(Mockito.anyInt(), Mockito.anyString(),Mockito.anyString()))
                .thenReturn(RestResponse.successWithoutMessage(getAUMForAdminBody()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/bi/aums/firms?page=0&previousDate=2017-01-01&currentDate=2017-01-01")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        log.info("JSON Response :::: "+mvcResult.getResponse().getContentAsString());
        JSONAssert.assertEquals(expected, mvcResult.getResponse().getContentAsString(), true);
    }

    @Test
    public void testGetAUMForFirm() throws Exception{

        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(new FileReader("src/test/resources/json/AUMForFirm.json"));
        String expected = data.toJSONString();

        Mockito.when(aumService.getAUMForFirm(Mockito.anyLong(), Mockito.anyString(),Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(RestResponse.successWithoutMessage(getAUMForFirmBody()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/bi/aums/advisors?firmId=283&page=0&previousDate=2017-04-01&currentDate=2017-04-01")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        log.info("JSON Response :::: "+mvcResult.getResponse().getContentAsString());
        JSONAssert.assertEquals(expected, mvcResult.getResponse().getContentAsString(), true);

    }

    @Test
    public void testGetAUMForAdvisor() throws Exception{

        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(new FileReader("src/test/resources/json/AUMForAdvisor.json"));
        String expected = data.toJSONString();

        Mockito.when(aumService.getAUMForAdvisor(Mockito.anyLong(), Mockito.anyString(),Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(RestResponse.successWithoutMessage(getAUMForAdvisorBody()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/bi/aums/clients?advisorId=283&page=0&previousDate=2017-04-01&currentDate=2017-04-01")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        log.info("JSON Response :::: "+mvcResult.getResponse().getContentAsString());
        JSONAssert.assertEquals(expected, mvcResult.getResponse().getContentAsString(), true);
    }

    @Test
    public void testGetAUMSummary() throws Exception{

        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(new FileReader("src/test/resources/json/AUMSummaryLanding.json"));
        String expected = data.toJSONString();

        Mockito.when(aumService.getAUMSummary()).thenReturn(RestResponse.successWithoutMessage(getAUMSummaryBody()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bi/aums")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        log.info("JSON Response :::: "+mvcResult.getResponse().getContentAsString());
        JSONAssert.assertEquals(expected, mvcResult.getResponse().getContentAsString(), true);
    }

    public AUMForAdmin getAUMForAdminBody(){

        List<FirmAUM> firmAUMList = new ArrayList<FirmAUM>();

        FirmAUM firmAUM = new FirmAUM();
        firmAUM.setFirmId(123l);
        firmAUM.setName("UnitTest");

        firmAUM.setCurrent(getAUMDiff());
        firmAUM.setPrevious(getAUMDiff());

        firmAUMList.add(firmAUM);
        firmAUMList.add(firmAUM);

        AUMForAdmin mockAUMForAdmin = new AUMForAdmin(2l, firmAUMList);
        return mockAUMForAdmin;
    }

    public AUMForFirm getAUMForFirmBody(){

        List<AdvisorAUM> advisorAUMList = new ArrayList<AdvisorAUM>();

        AdvisorAUM advisorAUM = new AdvisorAUM();
        advisorAUM.setAdvisorId(123l);
        advisorAUM.setName("UnitTest Advisor");

        advisorAUM.setCurrent(getAUMDiff());
        advisorAUM.setPrevious(getAUMDiff());

        advisorAUMList.add(advisorAUM);
        advisorAUMList.add(advisorAUM);

        AUMForFirm mockAUMForFirm = new AUMForFirm();
        mockAUMForFirm.setAdvisors(advisorAUMList);
        mockAUMForFirm.setTotalAdvisors(2l);
        return mockAUMForFirm;
    }

    public AUMForAdvisor getAUMForAdvisorBody(){

        List<ClientAUM> clientAUMList = new ArrayList<ClientAUM>();

        ClientAUM clientAUM = new ClientAUM();
        clientAUM.setClientId(13l);
        clientAUM.setName("UnitTest Client");

        clientAUM.setCurrent(getAUMDiff());
        clientAUM.setPrevious(getAUMDiff());

        clientAUMList.add(clientAUM);
        clientAUMList.add(clientAUM);

        AUMForAdvisor mockAUMForFirm = new AUMForAdvisor();
        mockAUMForFirm.setClients(clientAUMList);
        mockAUMForFirm.setTotalClients(2l);
        return mockAUMForFirm;
    }

    public List<AumDiff> getAUMSummaryBody(){

        List<AumDiff> aumDiffList = new ArrayList<AumDiff>();
        aumDiffList.add(getAUMDiff());
        aumDiffList.add(getAUMDiff());
        aumDiffList.add(getAUMDiff());
        return aumDiffList;
    }

    public AumDiff getAUMDiff(){

        AumDiff aumDiff = new AumDiff();
        aumDiff.setDate("2017-06-11");
        aumDiff.setTotal(new BigDecimal(287346));
        Map<String, BigDecimal> assetClass = new HashMap<>();
        assetClass.put("US Stocks", new BigDecimal(1782));
        assetClass.put("Non US Stocks", new BigDecimal(12345));
        assetClass.put("Cash", new BigDecimal(1234));
        aumDiff.setAssetClass(assetClass);
        return aumDiff;
    }
}
