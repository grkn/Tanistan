package com.friends.tanistan.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {


    private CustomAuthenticationProvider customAuthenticationProvider;
    private CustomUserDetailService customUserDetailService;
    private CustomBasicAuthenticationFilter customBasicAuthenticationFilter;
    private CustomAuthSuccessHandler customAuthSuccessHandler;

    public CustomWebSecurityConfigurerAdapter(CustomAuthenticationProvider customAuthenticationProvider,
            CustomUserDetailService customUserDetailService, CustomAuthSuccessHandler customAuthSuccessHandler,
            CustomBasicAuthenticationFilter customBasicAuthenticationFilter) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.customUserDetailService = customUserDetailService;
        this.customBasicAuthenticationFilter = customBasicAuthenticationFilter;
        this.customAuthSuccessHandler = customAuthSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable().authorizeRequests().antMatchers("/tanistan/**").fullyAuthenticated().and()
                .authenticationProvider(customAuthenticationProvider).userDetailsService(customUserDetailService)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().formLogin().loginProcessingUrl("/login")
                .usernameParameter("username").passwordParameter("password").successHandler(customAuthSuccessHandler);
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return customUserDetailService;
    }

}
