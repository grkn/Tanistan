package com.friends.tanistan.repository.impl;

import com.friends.tanistan.repository.OAuthClientDetailsRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class OAuthClientDetailsRepositoryImpl implements OAuthClientDetailsRepository {

    private final PasswordEncoder passwordEncoder;
    private final DataSource dataSource;

    public OAuthClientDetailsRepositoryImpl(PasswordEncoder passwordEncoder, DataSource dataSource) {
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
    }

    @Override
    public void insertAccessToken(String clientId, String clientSecret, String roles, int accessTokenValiditySecond,
            long refreshTokenValiditySecond) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String insertScript = "INSERT INTO OAUTH_CLIENT_DETAILS  "
                + "(client_id, client_secret, scope, authorized_grant_types,  "
                + "authorities, access_token_validity, refresh_token_validity) VALUES " + "(?,?, "
                + " 'read,write', 'password,refresh_token,client_credentials,authorization_code',?,?,?)";
        jdbcTemplate.update(insertScript, clientId, passwordEncoder.encode(clientSecret), roles,
                accessTokenValiditySecond, refreshTokenValiditySecond);
    }

    @Override
    public void updateAuthorities(List<String> authorities, String clientId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String updateScript = "UPDATE OAUTH_CLIENT_DETAILS SET authorities = ? WHERE client_id = ?";
        jdbcTemplate.update(updateScript, authorities.toString().substring(1, authorities.toString().length() - 1),
                clientId);
    }

    @Override
    public void updateClientIdAndClientSecret(String clientId, String clientSecret, String oldEmailAddress,
            boolean isEncodable) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String updateScript = "UPDATE OAUTH_CLIENT_DETAILS SET client_id = ?, client_secret = ? WHERE client_id = ?";
        jdbcTemplate
                .update(updateScript, clientId, isEncodable ? passwordEncoder.encode(clientSecret) : clientSecret,
                        oldEmailAddress);

        jdbcTemplate
                .update("UPDATE oauth_client_token SET client_id = ? WHERE client_id = ?", clientId, oldEmailAddress);
        jdbcTemplate
                .update("UPDATE oauth_access_token SET client_id = ? WHERE client_id = ?", clientId, oldEmailAddress);
    }

    @Override
    public Map<String, Object> getOAuthClientDetails(String client_id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String query = "SELECT * FROM OAUTH_CLIENT_DETAILS WHERE client_id = ?";
        return jdbcTemplate.queryForMap(query, client_id);
    }

}
