package com.friends.test.automation.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

@Configuration
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {

    private final CustomAuthenticationManager customAuthenticationManager;
    private final CustomUserDetailService customUserDetailService;
    private final DataSource dataSource;
    private final TokenStore tokenStore;
    private final JwtAccessTokenConverter accessTokenConverter;
    private final CustomBasicAuthenticationFilter customBasicAuthenticationFilter;


    public OAuth2Configuration(
            CustomAuthenticationManager customAuthenticationManager,
            CustomUserDetailService customUserDetailService, DataSource dataSource,
            TokenStore tokenStore,
            JwtAccessTokenConverter accessTokenConverter,
            CustomBasicAuthenticationFilter customBasicAuthenticationFilter) {
        this.customAuthenticationManager = customAuthenticationManager;
        this.customUserDetailService = customUserDetailService;
        this.dataSource = dataSource;
        this.tokenStore = tokenStore;
        this.accessTokenConverter = accessTokenConverter;
        this.customBasicAuthenticationFilter = customBasicAuthenticationFilter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(customAuthenticationManager).userDetailsService(customUserDetailService)
                .tokenStore(tokenStore).reuseRefreshTokens(false)
                .accessTokenConverter(accessTokenConverter);

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.addTokenEndpointAuthenticationFilter(customBasicAuthenticationFilter);
        oauthServer.tokenKeyAccess("hasAuthority('ROLE_ADMIN','ROLE_USER')")
                .checkTokenAccess("hasAuthority('ROLE_ADMIN','ROLE_USER')");
    }


}