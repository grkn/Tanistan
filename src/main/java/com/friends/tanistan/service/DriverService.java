package com.friends.tanistan.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.friends.tanistan.controller.driver.DeleteSessionResource;
import com.friends.tanistan.controller.driver.SessionDto;
import com.friends.tanistan.controller.driver.SessionResource;
import com.friends.tanistan.controller.driver.StatusResource;
import com.friends.tanistan.controller.resource.ErrorResource;
import com.friends.tanistan.exception.DriverException;
import com.google.gson.Gson;

@Service
public class DriverService extends BaseService {

	private final RestTemplate restTemplate;
	private final ConverterToJson converter;

	@Value("${chrome.driver.ip}")
	private String driverIp;

	@Value("${chrome.driver.port}")
	private String port;

	private static String DRIVER_URL;

	public DriverService(RestTemplate restTemplate, ConverterToJson converter) {
		this.restTemplate = restTemplate;
		this.converter = converter;
	}
	
	@PostConstruct
	public void setUp() {
		DRIVER_URL = driverIp + ":" + port;
	}
	
	@Component
	private final static class ConverterToJson {

		private final Gson gson;
		private String result;

		public ConverterToJson(Gson gson) {
			this.gson = gson;
		}

		public <T> T convert(String json, Class<T> clazz) {
			return gson.fromJson(json, clazz);
		}
	}

	public SessionResource getSession(SessionDto sessionDto) {
		HttpEntity<SessionDto> httpEntity = new HttpEntity<>(sessionDto);
		ResponseEntity<String> result = restTemplate.exchange(DRIVER_URL + "/session", HttpMethod.POST,
				httpEntity, String.class);
		throwDriverExceptionWhenResultIsEmpty(result);
		return converter.convert(result.getBody(),SessionResource.class);
	}

	public DeleteSessionResource deleteSession(String sessionId) {
		ResponseEntity<String> result = restTemplate.exchange(DRIVER_URL + "/session/{sessionId}", HttpMethod.GET,
				new HttpEntity(null), String.class,sessionId);
		throwDriverExceptionWhenResultIsEmpty(result);
		return converter.convert(result.getBody(), DeleteSessionResource.class);
	}

	public StatusResource getStatus() {
		ResponseEntity<String> result = restTemplate.exchange(DRIVER_URL + "/status", HttpMethod.GET,
				new HttpEntity(null), String.class);
		throwDriverExceptionWhenResultIsEmpty(result);
		return converter.convert(result.getBody(), StatusResource.class);
	}
	
	private void throwDriverExceptionWhenResultIsEmpty(ResponseEntity<String> result) {
		if(StringUtils.isEmpty(result.getBody())) {
			throw new DriverException(ErrorResource.ErrorContent.builder().message("getSession() method -> Driver did not return any response").build(""));
		}
	}
}
