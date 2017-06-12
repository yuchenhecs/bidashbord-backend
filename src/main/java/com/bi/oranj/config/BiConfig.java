package com.bi.oranj.config;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * Created by harshavardhanpatil on 5/25/17.
 */
@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(basePackages = "com.bi.oranj.repository.bi",
        entityManagerFactoryRef = "biEntityManager",
        transactionManagerRef = "biTransactionManager")
public class BiConfig {

    @Autowired
    private Environment env;

    public BiConfig() {
        super();
    }

    @Bean (name = "biEntityManager")
    @Primary
    @FlywayDataSource
    public LocalContainerEntityManagerFactoryBean biEntityManager() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(biDataSource());
        em.setPackagesToScan(new String[] { "com.bi.oranj.model.bi", "com.bi.oranj.model.bi.OranjAum" });

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean
    @Primary
    @FlywayDataSource
    public DataSource biDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Preconditions.checkNotNull(env.getProperty("jdbc.driverClassName")));
        dataSource.setUrl(Preconditions.checkNotNull(env.getProperty("bi.jdbc.url")));
        dataSource.setUsername(Preconditions.checkNotNull(env.getProperty("bi.jdbc.user")));
        dataSource.setPassword(Preconditions.checkNotNull(env.getProperty("bi.jdbc.pass")));
        return dataSource;
    }

    @Bean
    @Primary
    @FlywayDataSource
    public PlatformTransactionManager biTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(biEntityManager().getObject());
        return transactionManager;
    }
}
