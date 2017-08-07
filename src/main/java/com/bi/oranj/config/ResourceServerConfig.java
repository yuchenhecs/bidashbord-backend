package com.bi.oranj.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .antMatchers(HttpMethod.GET, "/**/grid-config/**").permitAll()
                .antMatchers(HttpMethod.POST, "/**/grid-config/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/v2/api-docs", "/swagger-resources/configuration/ui", "/swagger-resources", "/swagger-resources/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**/config/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**/analytics/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**/oranj/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**/aums").hasAnyAuthority("SuperAdmin", "FirmAdmin", "Advisor")
                .antMatchers(HttpMethod.GET, "/**/aums/firms").hasAnyAuthority("SuperAdmin")
                .antMatchers(HttpMethod.GET, "/**/aums/advisors").hasAnyAuthority("SuperAdmin", "FirmAdmin")
                .antMatchers(HttpMethod.GET, "/**/aums/clients").hasAnyAuthority("SuperAdmin", "FirmAdmin", "Advisor")
                .antMatchers(HttpMethod.GET, "/**/goals").hasAnyAuthority("SuperAdmin", "FirmAdmin", "Advisor")
                .antMatchers(HttpMethod.GET, "/**/goals/firms").hasAnyAuthority("SuperAdmin")
                .antMatchers(HttpMethod.GET, "/**/goals/advisors").hasAnyAuthority("SuperAdmin", "FirmAdmin")
                .antMatchers(HttpMethod.GET, "/**/goals/clients").hasAnyAuthority("SuperAdmin", "FirmAdmin", "Advisor")
                .antMatchers(HttpMethod.GET, "/**/stats").hasAnyAuthority("SuperAdmin", "FirmAdmin", "Advisor")
                .antMatchers(HttpMethod.GET, "/**/stats/firms").hasAnyAuthority("SuperAdmin")
                .antMatchers(HttpMethod.GET, "/**/stats/advisors").hasAnyAuthority("SuperAdmin", "FirmAdmin")
                .antMatchers(HttpMethod.GET, "/**/stats/clients").hasAnyAuthority("SuperAdmin", "FirmAdmin", "Advisor")
                .antMatchers(HttpMethod.GET, "/**/networth").hasAnyAuthority("SuperAdmin", "FirmAdmin", "Advisor")
                .antMatchers(HttpMethod.GET, "/**/networth/firms").hasAnyAuthority("SuperAdmin")
                .antMatchers(HttpMethod.GET, "/**/networth/advisors").hasAnyAuthority("SuperAdmin", "FirmAdmin")
                .antMatchers(HttpMethod.GET, "/**/networth/clients").hasAnyAuthority("SuperAdmin", "FirmAdmin", "Advisor")
                .antMatchers(HttpMethod.GET, "/**/gamification/**").hasAnyAuthority("Advisor", "FirmAdmin")
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
