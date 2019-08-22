package com.friends.tanistan.repository;

import java.util.List;
import java.util.Map;

public interface OAuthClientDetailsRepository {

    void insertAccessToken(String clientId, String clientSecret, String roles, int accessTokenValiditySeconds,
            long refreshTokenValiditySeconds);

    void updateAuthorities(List<String> authorities, String clientId);

    void updateClientIdAndClientSecret(String clientId, String clientSecret, String oldEmailAddress,
            boolean isEncodable);

    Map<String, Object> getOAuthClientDetails(String client_id);
}
