package com.bi.oranj.service;

import com.bi.oranj.service.bi.AUMService;
import com.bi.oranj.utils.date.DateUtility;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AUMServiceTest {

    @Autowired
    private AUMService aumService;

    @Autowired
    DateUtility dateUtility;

    @Test()
    public void testGetQuarterFirstDates(){
        List<String> dateList = dateUtility.getQuarterFirstDates();
        Assert.assertNotNull(dateList);
        Assert.assertEquals(dateList.get(0), "2014-01-01");
    }

}
