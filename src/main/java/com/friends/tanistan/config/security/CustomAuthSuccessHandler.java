package com.friends.tanistan.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friends.tanistan.controller.resource.UserResource;
import com.friends.tanistan.controller.resource.UserResourceWithAccessToken;
import com.friends.tanistan.entity.UserEntity;
import com.friends.tanistan.repository.OAuthClientDetailsRepository;
import com.friends.tanistan.service.UserService;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final UserService<UserEntity> userService;
    private final ConversionService conversionService;
    private final TokenStore tokenStore;
    private final OAuthClientDetailsRepository oAuthClientDetailsRepository;


    public CustomAuthSuccessHandler(ObjectMapper objectMapper,
            UserService<UserEntity> userService, ConversionService conversionService,
            OAuthClientDetailsRepository oAuthClientDetailsRepository,
            TokenStore tokenStore,
            OAuthClientDetailsRepository oAuthClientDetailsRepository1) {
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.conversionService = conversionService;
        this.tokenStore = tokenStore;
        this.oAuthClientDetailsRepository = oAuthClientDetailsRepository1;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        UserResource userResource = conversionService
                .convert(userService.getUserByUsernameOrEmail(authentication.getPrincipal().toString(),
                        authentication.getPrincipal().toString()), UserResource.class);
        UserResourceWithAccessToken userResourceWithAccessToken = new UserResourceWithAccessToken(userResource);

        OAuth2Request oAuth2Request = prepareOAuth2Request(authentication);

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request,
                SecurityContextHolder.getContext().getAuthentication());

        userResourceWithAccessToken.setAccessToken(tokenStore.getAccessToken(oAuth2Authentication).getValue());

        response.getWriter().write(objectMapper.writeValueAsString(userResourceWithAccessToken) + "\n");
        response.setHeader("Content-Type", "application/json");

        if (!response.getHeaders("Access-Control-Allow-Origin").contains("*")) {
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "*");
            response.addHeader("Access-Control-Allow-Headers", "*");
            response.addHeader("Access-Control-Expose-Headers",
                    "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addIntHeader("Access-Control-Max-Age", 100);
        }
        response.flushBuffer();
    }

    private OAuth2Request prepareOAuth2Request(Authentication authentication) {
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("client_id", authentication.getPrincipal().toString());
        requestParameters.put("client_secret", authentication.getCredentials().toString());

        return new OAuth2Request(requestParameters, null, null, true, null, null,
                null, null, null);
    }
}
