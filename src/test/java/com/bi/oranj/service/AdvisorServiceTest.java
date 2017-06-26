package com.bi.oranj.service;

import com.bi.oranj.repository.bi.AdvisorRepository;
import com.bi.oranj.service.bi.AdvisorService;
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
public class AdvisorServiceTest {
    private AdvisorService advisorService;
    private AdvisorRepository advisorRepositoryMock;

    @Value("${firmId}")
    private Long firmId;

    @Before
    public void setUp(){
        advisorRepositoryMock = Mockito.mock(AdvisorRepository.class);
        advisorService = new AdvisorService();
    }

    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesTest() throws Exception{
        int response = advisorService.totalPages(firmId);
        Assert.assertTrue(response + " should be greater than " + -1, -1 < response);
    }

    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesWithStartDate() throws Exception{
        int response = advisorService.totalPagesWithStartDate(firmId, "2017-05-12");
        Assert.assertTrue(response + " should be greater than " + -1, -1 < response);
    }

    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesWithEndDate () throws Exception {
        int response = advisorService.totalPagesWithEndDate(firmId, "2017-05-12");
        Assert.assertTrue(response + " should be greater than " + -1, -1 < response);
    }

    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesByDateBetween () throws Exception {
        int response = advisorService.totalPagesByDateBetween(firmId, "2017-05-12","2017-06-16");
        Assert.assertTrue(response + " should be greater than " + -1, -1 < response);
    }

}
