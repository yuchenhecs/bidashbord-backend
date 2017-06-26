package com.bi.oranj.service;

import com.bi.oranj.repository.bi.FirmRepository;
import com.bi.oranj.service.bi.FirmService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * For this Unit test, number of elements in one page assumed as 100.
 * So, if in DB <= 100 records it will return 0, which means there is only 1 page.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FirmServiceTest {

    private FirmService firmService;
    private FirmRepository firmRepositoryMock;

    @Before
    public void setUp(){
        firmRepositoryMock = Mockito.mock(FirmRepository.class);
        firmService = new FirmService();
    }

    @Value("${firmId}")
    private Long firmId;

    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesTest() throws Exception{
        int response = firmService.totalPages();
        Assert.assertTrue(response + " should be greater than " + -1, -1 < response);
    }

    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesWithStartDate() throws Exception{
        int response = firmService.totalPagesWithStartDate(firmId, "2017-05-12");
        Assert.assertTrue(response + " should be greater than " + -1, -1 < response);
    }

    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesWithEndDate () throws Exception {
        int response = firmService.totalPagesWithEndDate(firmId, "2017-05-12");
        Assert.assertTrue(response + " should be greater than " + -1, -1 < response);
    }

    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesByDateBetween () throws Exception {
        int response = firmService.totalPagesByDateBetween(firmId, "2017-05-12","2017-06-16");
        Assert.assertTrue(response + " should be greater than " + -1, -1 < response);
    }


}
