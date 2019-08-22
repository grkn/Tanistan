package com.friends.tanistan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableAuthorizationServer
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // Signing will be done in cloud variables so ignore currently
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("MaYzkSjmkzPC57L");
        return converter;
    }

    private String generateKey(OAuth2Request oAuth2Request) {
        MessageDigest digest;
        try {
            String clientId = oAuth2Request.getRequestParameters().get("client_id");
            if (oAuth2Request.getRequestParameters().get("client_secret") != null) {
                String clientSecret = oAuth2Request.getRequestParameters().get("client_secret");
                digest = MessageDigest.getInstance("MD5");
                byte[] bytes = digest.digest((clientId + ":" + clientSecret).getBytes("UTF-8"));
                return String.format("%032x", new BigInteger(1, bytes));
            } else {
                String scope = oAuth2Request.getRequestParameters().get("scope");
                String auths = oAuth2Request.getRequestParameters().get("auths");
                digest = MessageDigest.getInstance("MD5");

                byte[] bytes = digest.digest((clientId + ":" + scope + ':' + auths).getBytes("UTF-8"));
                return String.format("%032x", new BigInteger(1, bytes));
            }

        } catch (NoSuchAlgorithmException nsae) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).", nsae);
        } catch (UnsupportedEncodingException uee) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).", uee);
        }
    }

    @Bean
    public TokenStore tokenStore() {
        JdbcTokenStore jdbcTokenStore = new JdbcTokenStore(dataSource);
        jdbcTokenStore.setAuthenticationKeyGenerator(authentication -> {
            Map<String, String> params = new HashMap<>();
            params.put("client_id", authentication.getPrincipal().toString());

            if (authentication.getUserAuthentication() != null) {
                params.put("client_secret", authentication.getUserAuthentication().getCredentials().toString());
            } else {
                params.put("scope", new TreeSet<>(authentication.getOAuth2Request().getScope()).toString());
                Collection<String> grantedAuthoritySet = authentication.getOAuth2Request().getAuthorities().stream()
                        .map(item -> item.getAuthority()).collect(
                                Collectors.toSet());
                params.put("auths", new TreeSet<>(grantedAuthoritySet).toString());
            }

            OAuth2Request oAuth2Request = authentication.getOAuth2Request().createOAuth2Request(params);
            return generateKey(oAuth2Request);
        });
        return jdbcTokenStore;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

}