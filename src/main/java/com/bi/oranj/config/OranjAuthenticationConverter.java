package com.bi.oranj.config;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

public class OranjAuthenticationConverter  implements UserAuthenticationConverter {
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        return null;
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if(map.containsKey("user_name")) {
            Object principal = map.get("user_name");
            Collection authorities = this.getAuthorities(map);
            return new OranjAuthenticationToken(principal, "N/A", authorities, map.get("user_id"));
        } else {
            return null;
        }
    }

    private Collection getAuthorities(Map<String, ?> map) {
        Object authorities = map.get("authorities");
        if(authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String)authorities);
        } else if(authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.collectionToCommaDelimitedString((Collection)authorities));
        } else {
            throw new IllegalArgumentException("Authorities must be either a String or a Collection");
        }
    }
}
