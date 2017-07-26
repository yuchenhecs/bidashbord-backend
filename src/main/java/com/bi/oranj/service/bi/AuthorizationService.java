package com.bi.oranj.service.bi;

import com.bi.oranj.config.OranjAuthenticationToken;
import netscape.security.ForbiddenTargetException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorizationService {

    public Long getUserId() {

        Integer userId = (Integer) ((OranjAuthenticationToken)
                ((OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication())
                        .getUserAuthentication()).getUserId();
        if (userId == null) {
            throw new ForbiddenTargetException("user not found");
        }
        return userId.longValue();
    }

    public String getToken() {
        return ((OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getTokenValue();
    }

    public boolean isClient() {
        List<String> roles = getAuthorities();
        return roles.contains("Client") || roles.contains("Prospect");
    }

    public boolean isAdvisor() {
        List<String> roles = getAuthorities();
        return roles.contains("Advisor");
    }

    public boolean isAdmin() {
        List<String> roles = getAuthorities();
        return roles.contains("FirmAdmin");
    }

    public boolean hasPermission(String permission) {
        return getAuthorities().contains(permission);
    }

    private List<String> getAuthorities() {
        return ((OranjAuthenticationToken)
                ((OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication())
                        .getUserAuthentication()).getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
