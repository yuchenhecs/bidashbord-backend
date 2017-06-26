package com.bi.oranj.controller;

import com.bi.oranj.controller.bi.AUMController;
import com.bi.oranj.service.bi.AdvisorService;
import com.bi.oranj.service.bi.ClientService;
import com.bi.oranj.service.bi.FirmService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by jaloliddinbakirov on 6/20/17.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = AUMController.class, secure = false)
public class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FirmService firmService;

    @MockBean
    private AdvisorService advisorService;

    @MockBean
    private ClientService clientService;

    @Test
    public void processRequestTest() throws Exception{

    }

}
