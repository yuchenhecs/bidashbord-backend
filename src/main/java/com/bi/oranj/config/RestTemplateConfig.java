package com.bi.oranj.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    private static final Logger log = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

    @Bean
    public RestTemplate restTemplate() {
        ClientHttpRequestInterceptor interceptor = new LoggingRequestInterceptor();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(interceptor));
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        return restTemplate;
    }

    public static class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

            ClientHttpResponse response = execution.execute(request, body);
            log(request,body,response);
            return response;
        }

        private void log(HttpRequest request, byte[] body, ClientHttpResponse response) throws IOException {
            log.trace("Method {} for Call to {} on host {} with following Header {} and Body {}",
                    request.getMethod().toString(),
                    request.getURI().getPath(),
                    request.getURI().getHost(),
                    request.getHeaders().toString(),
                    new String(body, "UTF-8"));
        }
    }
}
