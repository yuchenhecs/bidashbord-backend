package com.bi.oranj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.*;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${authorizationservice.url}")
    private String authorizationUrl;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/v2/api-docs", "/swagger-resources/configuration/ui", "/swagger-resources", "/swagger-resources/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**/analytics/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**/aums/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**/gamification/**/summary").hasAuthority("HouseholdCreateTEST")
                .antMatchers(HttpMethod.GET, "/**/gamification/**/patOnTheBack").hasAuthority("HouseholdCreate")
                .antMatchers(HttpMethod.GET, "/**/goals/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**/stats/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**/networth/**").permitAll()
                .anyRequest().authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        OranjRemoteTokenServices remoteTokenServices = new OranjRemoteTokenServices();
        remoteTokenServices.setCheckTokenEndpointUrl(authorizationUrl + "/user/oranj");
        remoteTokenServices.setAccessTokenConverter(accessTokenConverter());
        resources.tokenServices(remoteTokenServices);
    }

    @Bean
    public AccessTokenConverter accessTokenConverter() {
        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
        defaultAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter());
        return defaultAccessTokenConverter;
    }

    @Bean
    public UserAuthenticationConverter userAuthenticationConverter() {
        return new OranjAuthenticationConverter();
    }

}
