package com.bi.oranj.controller;

import com.bi.oranj.controller.bi.AUMController;
import com.bi.oranj.service.bi.AUMService;
import org.junit.Before;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;

/**
 * Created by harshavardhanpatil on 6/15/17.
 */
public class AUMControllerTest {

    private MockMvc mockMvc;
    private AUMService mockAumService;

    @Before
    public void setup() {
        mockAumService = mock(AUMService.class);
//        mockMvc = MockMvcBuilders.standaloneSetup(new AUMController(mockAumService)).build();
    }
}
