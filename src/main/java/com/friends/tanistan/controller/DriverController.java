package com.friends.tanistan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friends.tanistan.controller.driver.SessionDto;
import com.friends.tanistan.controller.driver.SessionResorce;
import com.google.gson.Gson;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;

@RestController
@RequestMapping(value = "/tanistan/driver", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DriverController {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Gson gson;

    @Value("${chrome.driver.ip}")
    private String driverIp;

    @Value("${chrome.driver.port}")
    private String port;

    private static String DRIVER_URL;

    public DriverController(RestTemplate restTemplate,ObjectMapper objectMapper){
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        gson = new Gson();
    }

    @PostConstruct
    public void setUp() {
        DRIVER_URL = driverIp + ":" + port;
        SessionResorce sessionResorce = new SessionResorce();
        HttpEntity httpEntity = new HttpEntity(sessionResorce);
        ResponseEntity<String> responseEntity = restTemplate.exchange(DRIVER_URL, HttpMethod.POST, httpEntity,String.class);
        SessionDto sessionDto = gson.fromJson(responseEntity.getBody(), SessionDto.class);
        System.out.println(sessionDto);
    }

}
