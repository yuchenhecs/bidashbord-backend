//package com.bi.oranj.controller;
//
//import com.bi.oranj.controller.bi.NetWorthController;
//import com.bi.oranj.controller.bi.resp.RestResponse;
//import com.bi.oranj.model.bi.NetWorthAdmin;
//import com.bi.oranj.model.bi.NetWorthForAdmin;
//import com.bi.oranj.service.bi.NetWorthService;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.io.FileReader;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by robertyuan on 6/30/17.
// */
//@RunWith(SpringRunner.class)
//@WebMvcTest(value = NetWorthController.class, secure = false)
//class NetWorthControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private NetWorthService netWorthService;
//
//    private final Logger log = LoggerFactory.getLogger(this.getClass());
//
//    @Test
//    public void testgetNetWorthForAdmin() throws Exception {
//
//        //get expected result
//        JSONParser parser = new JSONParser();
//        JSONObject data = (JSONObject) parser.parse(new FileReader("src/test/resources/json/NetWorthForAdmin.json"));
//        String expected = data.toJSONString();
//
//        //mock calls the function
//        Mockito.when(netWorthService.getNetWorthForAdmin(Mockito.anyInt()))
//                .thenReturn(RestResponse.successWithoutMessage(getNetWorthForAdminBody()));
//
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .get("/bi/networth/firms?page=0")
//                .accept(MediaType.APPLICATION_JSON);
//
//        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
//
//        log.info("JSON Response :::: "+mvcResult.getResponse().getContentAsString());
//        JSONAssert.assertEquals(expected, mvcResult.getResponse().getContentAsString(), true);
//    }
//
//    public NetWorthAdmin getNetWorthForAdminBody() {
//        List<NetWorthForAdmin> firmList = new ArrayList<>();
//
//        NetWorthForAdmin netWorthForAdmin1 = new NetWorthForAdmin();
//        NetWorthForAdmin netWorthForAdmin2 = new NetWorthForAdmin();
//
//        netWorthForAdmin1.setFirmId(723);
//        netWorthForAdmin1.setName("0401002");
//        netWorthForAdmin1.setAbsNet(null);
//        netWorthForAdmin1.setAvgNet(null);
//
//        netWorthForAdmin2.setFirmId(510);
//        netWorthForAdmin2.setName("23andme");
//        netWorthForAdmin2.setAbsNet(0);
//        netWorthForAdmin2.setAvgNet(0);
//
//        firmList.add(netWorthForAdmin1);
//        firmList.add(netWorthForAdmin2);
//
//        NetWorthAdmin netWorthAdmin = new NetWorthAdmin();
//
//        netWorthAdmin.setFirms(firmList);
//
//        return netWorthAdmin;
//    }
//}
