package com.bi.oranj.service;

import com.bi.oranj.model.bi.NetWorth;
import com.bi.oranj.repository.bi.FirmRepository;
import com.bi.oranj.repository.bi.NetWorthRepository;
import com.bi.oranj.service.bi.FirmService;
import com.bi.oranj.service.oranj.OranjService;
import javafx.beans.binding.ObjectExpression;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;


/**
 * Created by yuchenhe on 6/26/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OranjServiceTest {

    private ArrayList<Object[]> testData = new ArrayList();

    @Autowired
    private OranjService netWorthService;

    private NetWorthRepository netWorthRepositoryMock;


    @Before
    public void setUp(){
        Object[] testEntry = new Object[6];
        testEntry[0] = BigInteger.valueOf(23);
        testEntry[1] = new Timestamp(System.currentTimeMillis() / 1000L);
        testEntry[2] = 1.2;
        testEntry[3] = BigInteger.valueOf(23);
        testEntry[4] = 1.3;
        testEntry[5] = 1.4;

        testData.add(testEntry);


        netWorthRepositoryMock = Mockito.mock(NetWorthRepository.class);
        netWorthService = new OranjService();

        netWorthService.netWorthRepository = netWorthRepositoryMock;

    }



    @Test
    public void test() throws Exception{


        netWorthService.storeNetWorth(testData);

        Mockito.verify(netWorthRepositoryMock).save(Mockito.any(NetWorth.class));

      //  Assert.assertTrue(true);
    }

}
