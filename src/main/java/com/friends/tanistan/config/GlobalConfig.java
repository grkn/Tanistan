package com.friends.tanistan.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friends.tanistan.controller.converter.UserAuthorizationToUserAuthorizationDtoConverter;
import com.friends.tanistan.controller.converter.UserDtoToUserEntityConverter;
import com.friends.tanistan.controller.converter.UserToUserResourceConverter;
import com.friends.tanistan.entity.UserEntity;
import com.friends.tanistan.service.UserService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Configuration
@EntityScan("com.friends.tanistan.entity")
@EnableJpaAuditing
@EnableJpaRepositories("com.friends.tanistan.repository")
public class GlobalConfig {

    @Bean
    @Primary
    public ConversionService conversionService() {
        GenericConversionService conversionService = new GenericConversionService();
        conversionService.addConverter(new UserToUserResourceConverter());
        conversionService.addConverter(new UserDtoToUserEntityConverter());
        conversionService.addConverter(new UserAuthorizationToUserAuthorizationDtoConverter());
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
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
