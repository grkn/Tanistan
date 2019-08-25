package com.friends.tanistan.controller;

import com.friends.tanistan.controller.driver.DefaultResource;
import com.friends.tanistan.controller.driver.DeleteSessionResource;
import com.friends.tanistan.controller.driver.FindElementDto;
import com.friends.tanistan.controller.driver.NavigateDto;
import com.friends.tanistan.controller.driver.SendKeysDto;
import com.friends.tanistan.controller.driver.SessionDto;
import com.friends.tanistan.controller.driver.SessionResource;
import com.friends.tanistan.controller.driver.TimeoutDto;
import com.friends.tanistan.service.DriverService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/tanistan/driver/session", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SessionController {

    private final DriverService driverService;

    public SessionController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public ResponseEntity<SessionResource> getSession(@RequestBody SessionDto sessionDto) {
        return driverService.getSession(sessionDto);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<DeleteSessionResource> deleteSession(@PathVariable String sessionId) {
        return driverService.deleteSession(sessionId);
    }

    @PostMapping("/{sessionId}/timeouts")
    public ResponseEntity<DefaultResource> setTimeout(@PathVariable String sessionId,
            @RequestBody TimeoutDto timeoutDto) {
        return driverService.setTimeout(sessionId, timeoutDto);
    }

    @PostMapping("/{sessionId}/url")
    public ResponseEntity<DefaultResource> setTimeout(@PathVariable String sessionId,
            @RequestBody NavigateDto navigateDto) {
        return driverService.navigate(sessionId, navigateDto);
    }

    @GetMapping("/{sessionId}/url")
    public ResponseEntity<DefaultResource> setTimeout(@PathVariable String sessionId) {
        return driverService.getCurrentUrl(sessionId);
    }

    @GetMapping("/{sessionId}/title")
    public ResponseEntity<DefaultResource> getPageTitle(@PathVariable String sessionId) {
        return driverService.getPageTitle(sessionId);
    }

    @PostMapping("/{sessionId}/window/current/maximize")
    public ResponseEntity<DefaultResource> maximize(@PathVariable String sessionId) {
        return driverService.maximize(sessionId);
    }

    @PostMapping("/{sessionId}/element")
    public ResponseEntity<DefaultResource> findElement(@PathVariable String sessionId,
            @RequestBody FindElementDto findElementDto) {
        return driverService.findElement(sessionId, findElementDto);
    }

    @GetMapping("/{sessionId}/element/{elementId}/selected")
    public ResponseEntity<DefaultResource> selected(@PathVariable("sessionId") String sessionId,
            @PathVariable("elementId") String elementId) {
        return driverService.isSelectedElement(sessionId, elementId);
    }

    @GetMapping("/{sessionId}/element/{elementId}/attribute/{name}")
    public ResponseEntity<DefaultResource> getAttributeByName(@PathVariable("sessionId") String sessionId,
            @PathVariable("elementId") String elementId, @PathVariable("name") String name) {
        return driverService.getAttributeByName(sessionId, elementId, name);
    }

    @GetMapping("/{sessionId}/element/{elementId}/text")
    public ResponseEntity<DefaultResource> getElementText(@PathVariable("sessionId") String sessionId,
            @PathVariable("elementId") String elementId) {
        return driverService.getElementText(sessionId, elementId);
    }

    @PostMapping("/{sessionId}/element/{elementId}/click")
    public ResponseEntity<DefaultResource> clickElement(@PathVariable("sessionId") String sessionId,
            @PathVariable("elementId") String elementId) {
        return driverService.clickElement(sessionId, elementId);
    }

    @PostMapping("/{sessionId}/element/{elementId}/value")
    public ResponseEntity<DefaultResource> sendKeys(@PathVariable("sessionId") String sessionId,
            @PathVariable("elementId") String elementId, @RequestBody SendKeysDto sendKeysDto) {
        return driverService.sendKeys(sessionId, elementId, sendKeysDto);
    }
}
