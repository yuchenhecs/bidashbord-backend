package com.bi.oranj.config;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.Assert;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class OranjRemoteTokenServices implements ResourceServerTokenServices {

    protected final Log logger = LogFactory.getLog(getClass());
    private RestTemplate restTemplate;
    private String checkTokenEndpointUrl;
    private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();

    public OranjRemoteTokenServices() {
        restTemplate = new RestTemplate();
        ClientHttpRequestInterceptor interceptor = new RestTemplateConfig.LoggingRequestInterceptor();
        restTemplate.setInterceptors(Collections.singletonList(interceptor));
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setCheckTokenEndpointUrl(String checkTokenEndpointUrl) {
        this.checkTokenEndpointUrl = checkTokenEndpointUrl;
    }

    public void setAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
        this.tokenConverter = accessTokenConverter;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            Map<String, Object> map = postForMap(checkTokenEndpointUrl, headers);

            if (map.containsKey("error")) {
                logger.debug("check_token returned error: " + map.get("error"));
                throw new InvalidTokenException(accessToken);
            }

            Assert.state(map.containsKey("client_id"), "Client id must be present in response from auth server");
            return tokenConverter.extractAuthentication(map);
        } catch (Exception e) {
            throw new InvalidTokenException(accessToken);
        }
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    private Map<String, Object> postForMap(String path, HttpHeaders headers) {
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        @SuppressWarnings("rawtypes")
        Map map = restTemplate.exchange(path, HttpMethod.GET, new HttpEntity<>(headers), Map.class).getBody();
        @SuppressWarnings("unchecked")
        Map<String, Object> result = map;
        return result;
    }
}
