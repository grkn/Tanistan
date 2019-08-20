package com.friends.tanistan.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friends.tanistan.controller.converter.UserAuthorizationToUserAuthorizationDtoConverter;
import com.friends.tanistan.controller.converter.UserDtoToUserEntityConverter;
import com.friends.tanistan.controller.converter.UserToUserResourceConverter;
import com.friends.tanistan.entity.UserEntity;
import com.friends.tanistan.service.UserService;
import com.google.gson.Gson;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
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
		RestTemplate restTemplate = new RestTemplate();
		GsonHttpMessageConverter converter = new GsonHttpMessageConverter(); 
		converter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)); 
		restTemplate.getMessageConverters().add(0, converter);
		return restTemplate;
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public Gson gson() {
		return new Gson();
	}
}
