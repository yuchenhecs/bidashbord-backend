package com.bi.oranj.service;

import com.bi.oranj.utils.InputValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InputValidatorTest {

    @Autowired
    private InputValidator inputValidator;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Test()
    public void testInValidInputDate() {
        boolean response = inputValidator.validateInputDate("sjdhz", "jdhskbjn");
        boolean expected = false;
        Assert.assertEquals(expected, response);
    }

    @Test()
    public void testValidInputDate() {
        boolean response = inputValidator.validateInputDate("2017-01-01", "2016-01-01");
        boolean expected = true;
        Assert.assertEquals(expected, response);
    }

    @Test()
    public void testInValidPageNumber() {
        boolean response = inputValidator.validateInputPageNumber(-12);
        boolean expected = false;
        Assert.assertEquals(expected, response);
    }

    @Test()
    public void testValidPageNumber() {

        Integer input = new Integer(2);
        Assert.assertEquals(true, inputValidator.validateInputPageNumber(input));
    }
}
