package com.bi.oranj.service;

import com.bi.oranj.service.bi.AUMService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AUMServiceTest {

    @Autowired
    private AUMService aumService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Test()
    public void testInValidInputDate() {
        boolean response = aumService.validateInputDate("sjdhz", "jdhskbjn");
        boolean expected = false;
        Assert.assertEquals(expected, response);
    }

    @Test()
    public void testValidInputDate() {
        boolean response = aumService.validateInputDate("2017-01-01", "2016-01-01");
        boolean expected = true;
        Assert.assertEquals(expected, response);
    }

    @Test()
    public void testInValidPageNumber() {
        boolean response = aumService.validateInputPageNumber(-12);
        boolean expected = false;
        Assert.assertEquals(expected, response);
    }

    @Test()
    public void testValidPageNumber() {

        Integer input = new Integer(2);
        Assert.assertEquals(true, aumService.validateInputPageNumber(input));
    }

    @Test()
    public void testGetQuarterFirstDates(){
        List<String> dateList = aumService.getQuarterFirstDates();
        Assert.assertNotNull(dateList);
        Assert.assertEquals(dateList.get(0), "2014-01-01");
    }

}
