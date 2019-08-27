package com.friends.test.automation.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.friends.test.automation.controller.converter.TestCaseDtoToTestCase;
import com.friends.test.automation.controller.converter.TestCaseInstanceRunnerToInstanceRunnerResource;
import com.friends.test.automation.controller.converter.TestCaseStepToStepResource;
import com.friends.test.automation.controller.converter.TestCaseToTestCaseResource;
import com.friends.test.automation.controller.converter.TestSuiteDtoToTestSuite;
import com.friends.test.automation.controller.converter.TestSuiteToTestSuiteResource;
import com.friends.test.automation.controller.converter.UserAuthorizationToUserAuthorizationDtoConverter;
import com.friends.test.automation.controller.converter.UserDtoToUserEntityConverter;
import com.friends.test.automation.controller.converter.UserToUserResourceConverter;
import com.friends.test.automation.entity.UserEntity;
import com.friends.test.automation.service.UserService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Configuration
@EntityScan("com.friends.test.automation.entity")
@EnableJpaAuditing
@EnableJpaRepositories("com.friends.test.automation")
public class GlobalConfig {

    @Bean
    @Primary
    public ConversionService conversionService() {
        GenericConversionService conversionService = new GenericConversionService();
        conversionService.addConverter(new TestCaseToTestCaseResource());
        conversionService.addConverter(new TestCaseDtoToTestCase());
        conversionService.addConverter(new UserToUserResourceConverter());
        conversionService.addConverter(new UserDtoToUserEntityConverter());
        conversionService.addConverter(new UserAuthorizationToUserAuthorizationDtoConverter());
        conversionService.addConverter(new TestSuiteDtoToTestSuite(new TestCaseDtoToTestCase()));
        conversionService.addConverter(new TestSuiteToTestSuiteResource(new TestCaseToTestCaseResource()));
        conversionService.addConverter(new TestCaseStepToStepResource());
        conversionService
                .addConverter(new TestCaseInstanceRunnerToInstanceRunnerResource(new TestCaseStepToStepResource()));
        return conversionService;
    }

    @Component
    public class TanistanAuditing implements AuditorAware<String> {

        private final UserService<UserEntity> userService;

        public TanistanAuditing(UserService<UserEntity> userService) {
            this.userService = userService;
        }

        @Override
        public Optional<String> getCurrentAuditor() {
            return Optional.of(userService.getCurrentUser());
        }

    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

}
