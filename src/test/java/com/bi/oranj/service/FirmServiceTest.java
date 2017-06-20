package com.bi.oranj.service;

import com.bi.oranj.repository.bi.FirmRepository;
import com.bi.oranj.service.bi.FirmService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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


    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesTest() throws Exception{
        int response = firmService.totalPages(1479);
        int expected = 0;
        Assert.assertEquals(expected, response);
    }

    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesWithStartDate() throws Exception{
        int response = firmService.totalPagesWithStartDate(1479, "2017-05-12");
        int expected = 0;
        Assert.assertEquals(expected, response);
    }

    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesWithEndDate () throws Exception {
        int response = firmService.totalPagesWithEndDate(1479, "2017-05-12");
        int expected = 0;
        Assert.assertEquals(expected, response);
    }

    /**
     * pagination starts with 0, so if totalPages returns 0, that means
     * there is 1 page;
     * @throws Exception
     */
    @Test
    public void totalPagesByDateBetween () throws Exception {
        int response = firmService.totalPagesByDateBetween(1479, "2017-05-12","2017-06-16");
        int expected = 0;
        Assert.assertEquals(expected, response);
    }



}
