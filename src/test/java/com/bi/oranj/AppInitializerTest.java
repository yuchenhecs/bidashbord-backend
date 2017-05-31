//package com.bi.oranj;
//
//import com.bi.oranj.config.BiConfig;
//import com.bi.oranj.bi.domain.BiRespository;
//import com.bi.oranj.or.OrConfig;
//import com.bi.oranj.or.domain.OranjGoal;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Created by harshavardhanpatil on 5/23/17.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = { OrConfig.class, BiConfig.class })
//@TransactionConfiguration
//public class AppInitializerTest {
//
//    @Autowired
//    private BiRespository biRespository;
//
//    @Autowired
//    private OrConfig orConfig;
//
//    @Test
//    @Transactional("userTransactionManager")
//    public void getGoals() {
////        assertNotNull(orConfig);
//    }
//
//
//    @Test
//    @Transactional("productTransactionManager")
//    public void whenCreatingProduct_thenCreated() {
//        Product product = new Product();
//        product.setName("Book");
//        product.setId(2);
//        product.setPrice(20);
//        product = productRepository.save(product);
//
//        assertNotNull(productRepository.findOne(product.getId()));
//    }
//}
