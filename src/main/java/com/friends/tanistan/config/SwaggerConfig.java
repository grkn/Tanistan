package com.friends.tanistan.config;

import com.friends.tanistan.tobedeleted.InitializeUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;

@Profile("swagger")
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final InitializeUser initializeUser;
    private static final String SWAGGER_CLIENT_ID = "swagger2";
    private static final String SWAGGER_CLIENT_SCREET = "swagger2";

    public SwaggerConfig(InitializeUser initializeUser) {
        this.initializeUser = initializeUser;
    }

    @PostConstruct
    public void setUp() {
        initializeUser.createPreDefinedUser(SWAGGER_CLIENT_ID, SWAGGER_CLIENT_SCREET, false);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.friends.tanistan.controller"))
                .paths(PathSelectors.ant("/tanistan/*"))
                .build()
                .securitySchemes(Collections.singletonList(securityScheme()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(SWAGGER_CLIENT_ID)
                .clientSecret(SWAGGER_CLIENT_SCREET)
                .scopeSeparator(" ")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    private SecurityScheme securityScheme() {
        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpoint("http://localhost:8082/oauth/token", "oauthtoken"))
                .tokenRequestEndpoint(
                        new TokenRequestEndpoint("http://localhost:8082/oauth/authorize", SWAGGER_CLIENT_ID,
                                SWAGGER_CLIENT_SCREET))
                .build();

        AuthorizationScope[] scopes = {
                new AuthorizationScope("read", "for read operations"),
                new AuthorizationScope("write", "for write operations")
        };

        SecurityScheme oauth = new OAuthBuilder().name("spring_oauth")
                .grantTypes(Arrays.asList(grantType))
                .scopes(Arrays.asList(scopes))
                .build();
        return oauth;
    }

    private SecurityContext securityContext() {

        AuthorizationScope[] scopes = {
                new AuthorizationScope("read", "for read operations"),
                new AuthorizationScope("write", "for write operations")
        };

        return SecurityContext.builder()
                .securityReferences(
                        Arrays.asList(new SecurityReference("spring_oauth", scopes)))
                .forPaths(PathSelectors.regex("/tanistan/*"))
                .build();
    }
}