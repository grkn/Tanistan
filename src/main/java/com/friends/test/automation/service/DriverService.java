package com.friends.test.automation.service;

import com.friends.test.automation.controller.resource.DefaultResource;
import com.friends.test.automation.controller.resource.DeleteSessionResource;
import com.friends.test.automation.controller.dto.FindElementDto;
import com.friends.test.automation.controller.dto.NavigateDto;
import com.friends.test.automation.controller.dto.SendKeysDto;
import com.friends.test.automation.controller.dto.SessionDto;
import com.friends.test.automation.controller.resource.SessionResource;
import com.friends.test.automation.controller.resource.StatusResource;
import com.friends.test.automation.controller.dto.TimeoutDto;
import com.friends.test.automation.controller.resource.ErrorResource;
import com.friends.test.automation.exception.DriverException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class DriverService extends BaseService {

    private final RestTemplate restTemplate;

    @Value("${chrome.driver.ip}")
    private String driverIp;

    @Value("${chrome.driver.port}")
    private String port;

    private static String DRIVER_URL;

    public DriverService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void setUp() {
        DRIVER_URL = driverIp + ":" + port;
    }

    public ResponseEntity<SessionResource> getSession(SessionDto sessionDto) {
        HttpEntity<SessionDto> httpEntity = new HttpEntity<>(sessionDto);
        ResponseEntity<SessionResource> result = restTemplate.exchange(DRIVER_URL + "/session", HttpMethod.POST,
                httpEntity, SessionResource.class);
        throwDriverExceptionWhenResultIsNull(result);
        return ResponseEntity.ok(result.getBody());
    }

    public ResponseEntity<DeleteSessionResource> deleteSession(String sessionId) {
        HttpEntity httpEntity = new HttpEntity<>(null);
        ResponseEntity<DeleteSessionResource> result = restTemplate
                .exchange(DRIVER_URL + "/session/{sessionId}", HttpMethod.DELETE,
                        httpEntity, DeleteSessionResource.class, sessionId);
        throwDriverExceptionWhenResultIsNull(result);
        return ResponseEntity.ok(result.getBody());
    }

    public ResponseEntity<StatusResource> getStatus() {
        HttpEntity httpEntity = new HttpEntity<>(null);
        ResponseEntity<StatusResource> result = restTemplate.exchange(DRIVER_URL + "/status", HttpMethod.GET,
                httpEntity, StatusResource.class);
        throwDriverExceptionWhenResultIsNull(result);
        return ResponseEntity.ok(result.getBody());
    }

    //POST /session/<session id>/timeouts
    public ResponseEntity<DefaultResource> setTimeout(String sessionId, TimeoutDto timeoutDto) {
        HttpEntity httpEntity = new HttpEntity<>(timeoutDto);
        ResponseEntity<DefaultResource> result = restTemplate
                .exchange(DRIVER_URL + "/session/{sessionId}/timeouts", HttpMethod.POST,
                        httpEntity, DefaultResource.class, sessionId);
        throwDriverExceptionWhenResultIsNull(result);
        return ResponseEntity.ok(result.getBody());
    }

    private void throwDriverExceptionWhenResultIsNull(ResponseEntity result) {
        if (result.getBody() == null) {
            throw new DriverException(ErrorResource.ErrorContent.builder()
                    .message("getSession() method -> Driver did not return any response").build(""));
        }
    }

    public ResponseEntity<DefaultResource> navigate(String sessionId, NavigateDto navigateDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity<>(navigateDto, httpHeaders);
        ResponseEntity<DefaultResource> result = restTemplate
                .exchange(DRIVER_URL + "/session/{sessionId}/url", HttpMethod.POST,
                        httpEntity, DefaultResource.class, sessionId);
        throwDriverExceptionWhenResultIsNull(result);
        return ResponseEntity.ok(result.getBody());
    }

    public ResponseEntity<DefaultResource> getCurrentUrl(String sessionId) {
        HttpEntity httpEntity = prepareContentType();
        ResponseEntity<DefaultResource> result = restTemplate
                .exchange(DRIVER_URL + "/session/{sessionId}/url", HttpMethod.GET,
                        httpEntity, DefaultResource.class, sessionId);
        throwDriverExceptionWhenResultIsNull(result);
        return ResponseEntity.ok(result.getBody());
    }

    public ResponseEntity<DefaultResource> getPageTitle(String sessionId) {
        HttpEntity httpEntity = prepareContentType();
        ResponseEntity<DefaultResource> result = restTemplate
                .exchange(DRIVER_URL + "/session/{sessionId}/title", HttpMethod.GET,
                        httpEntity, DefaultResource.class, sessionId);
        throwDriverExceptionWhenResultIsNull(result);
        return ResponseEntity.ok(result.getBody());
    }

    private HttpEntity prepareContentType() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(httpHeaders);
    }

    public ResponseEntity<DefaultResource> maximize(String sessionId) {
        HttpEntity httpEntity = prepareContentType();
        ResponseEntity<DefaultResource> result = restTemplate
                .exchange(DRIVER_URL + "/session/{sessionId}/window/current/maximize", HttpMethod.POST,
                        httpEntity, DefaultResource.class, sessionId);
        throwDriverExceptionWhenResultIsNull(result);
        return ResponseEntity.ok(result.getBody());
    }

    public ResponseEntity<DefaultResource> findElement(String sessionId, FindElementDto findElementDto) {
        HttpEntity<FindElementDto> httpEntity = new HttpEntity<>(findElementDto);
        ResponseEntity<DefaultResource> result = restTemplate
                .exchange(DRIVER_URL + "/session/{sessionId}/element", HttpMethod.POST,
                        httpEntity, DefaultResource.class, sessionId);
        throwDriverExceptionWhenResultIsNull(result);
        return ResponseEntity.ok(result.getBody());
    }

    public ResponseEntity<DefaultResource> isSelectedElement(String sessionId, String elementId) {
        HttpEntity httpEntity = prepareContentType();
        ResponseEntity<DefaultResource> result = restTemplate
                .exchange(DRIVER_URL + "/session/{sessionId}/element/{{elementId}}/selected", HttpMethod.GET,
                        httpEntity, DefaultResource.class, sessionId, elementId);
        throwDriverExceptionWhenResultIsNull(result);
        return ResponseEntity.ok(result.getBody());

    }

    public ResponseEntity<DefaultResource> getAttributeByName(String sessionId, String elementId, String name) {
        HttpEntity httpEntity = prepareContentType();
        ResponseEntity<DefaultResource> result = restTemplate
                .exchange(DRIVER_URL + "/session/{session_id}/element/{element_id}/attribute/{name}", HttpMethod.GET,
                        httpEntity, DefaultResource.class, sessionId, elementId, name);
        throwDriverExceptionWhenResultIsNull(result);
        return ResponseEntity.ok(result.getBody());
    }

    public ResponseEntity<DefaultResource> getElementText(String sessionId, String elementId) {
        HttpEntity httpEntity = prepareContentType();
        ResponseEntity<DefaultResource> result = restTemplate
                .exchange(DRIVER_URL + "/session/{session_id}/element/{element_id}/text", HttpMethod.GET,
                        httpEntity, DefaultResource.class, sessionId, elementId);
        throwDriverExceptionWhenResultIsNull(result);
        return ResponseEntity.ok(result.getBody());
    }

    public ResponseEntity<DefaultResource> clickElement(String sessionId, String elementId) {
        HttpEntity httpEntity = prepareContentType();
        ResponseEntity<DefaultResource> result = restTemplate
                .exchange(DRIVER_URL + "/session/{session_id}/element/{element_id}/click", HttpMethod.POST,
                        httpEntity, DefaultResource.class, sessionId, elementId);
        throwDriverExceptionWhenResultIsNull(result);
        return ResponseEntity.ok(result.getBody());
    }

    public ResponseEntity<DefaultResource> sendKeys(String sessionId, String elementId, SendKeysDto sendKeysDto) {
        HttpEntity httpEntity = new HttpEntity(sendKeysDto);
        ResponseEntity<DefaultResource> result = restTemplate
                .exchange(DRIVER_URL + "/session/{session_id}/element/{element_id}/value", HttpMethod.POST,
                        httpEntity, DefaultResource.class, sessionId, elementId);
        throwDriverExceptionWhenResultIsNull(result);
        return ResponseEntity.ok(result.getBody());
    }
}
