package com.friends.test.automation.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class SecurityUtil {

    public static OAuth2Request prepareOAuth2Request(Authentication authentication) {
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("client_id", authentication.getPrincipal().toString());
        requestParameters.put("client_secret", authentication.getCredentials().toString());

        String clientId = authentication.getPrincipal().toString();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Set<String> responseType = new HashSet<>();

        Set<String> scope = new HashSet<>();
        scope.add("write");
        scope.add("read");

        return new OAuth2Request(requestParameters, clientId, authorities, true, scope, null,
                null, responseType, null);
    }
}
