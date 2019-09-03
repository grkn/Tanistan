package com.friends.test.automation.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomBasicAuthenticationFilter customBasicAuthenticationFilter;
    private final CustomAuthenticationManager customBasicAuthenticationManager;
    private final CustomAuthSuccessHandler customAuthSuccessHandler;
    private final com.friends.test.automation.config.security.CorsFilter corsFilter;

    public ResourceServerConfig(CustomAuthenticationProvider customAuthenticationProvider,
            CustomBasicAuthenticationFilter customBasicAuthenticationFilter,
            CustomAuthenticationManager customBasicAuthenticationManager,
            CustomAuthSuccessHandler customAuthSuccessHandler,
            com.friends.test.automation.config.security.CorsFilter corsFilter) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.customBasicAuthenticationFilter = customBasicAuthenticationFilter;
        this.customBasicAuthenticationManager = customBasicAuthenticationManager;
        this.customAuthSuccessHandler = customAuthSuccessHandler;
        this.corsFilter = corsFilter;
    }

    @Autowired
    private ResourceServerTokenServices tokenServices;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(tokenServices);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter();
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(customAuthSuccessHandler);
        usernamePasswordAuthenticationFilter.setAuthenticationManager(customBasicAuthenticationManager);

        http.cors().disable().authenticationProvider(customAuthenticationProvider)
                .requestMatchers().and().authorizeRequests()
                .antMatchers("/actuator/**", "/api-docs/**", "/oauth/*","/register/**").permitAll()
                .and().requestMatchers().and().authorizeRequests().antMatchers("/tanistan/**")
                .fullyAuthenticated().and()
                .addFilterBefore(customBasicAuthenticationFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(usernamePasswordAuthenticationFilter, LogoutFilter.class)
                .addFilterAt(corsFilter, CorsFilter.class);
    }

}
