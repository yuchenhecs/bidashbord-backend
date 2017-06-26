package com.bi.oranj.service;

import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.repository.bi.ClientRepository;
import com.bi.oranj.service.bi.AdvisorService;
import com.bi.oranj.service.bi.ClientService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by jaloliddinbakirov on 6/20/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientServiceTest {
    private ClientService clientService;
    private ClientRepository clientRepositoryMock;

    @Value("${advisorId}")
    private Long advisorId;

    @Before
    public void setUp(){
        clientRepositoryMock = Mockito.mock(ClientRepository.class);
        clientService = new ClientService();
    }

    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesTest() throws Exception{
        int response = clientService.totalPages(advisorId);
        Assert.assertTrue(response + " should be greater than " + -1, -1 < response);
    }

    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesWithStartDate() throws Exception{
        int response = clientService.totalPagesWithStartDate(advisorId, "2017-05-12");
        Assert.assertTrue(response + " should be greater than " + -1, -1 < response);
    }

    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesWithEndDate () throws Exception {
        int response = clientService.totalPagesWithEndDate(advisorId, "2017-05-12");
        Assert.assertTrue(response + " should be greater than " + -1, -1 < response);
    }

    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesByDateBetween () throws Exception {
        int response = clientService.totalPagesByDateBetween(advisorId, "2017-05-12","2017-06-16");
        Assert.assertTrue(response + " should be greater than " + -1, -1 < response);
    }

}
