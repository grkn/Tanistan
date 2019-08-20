package com.friends.tanistan.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.friends.tanistan.controller.driver.DeleteSessionResource;
import com.friends.tanistan.controller.driver.SessionDto;
import com.friends.tanistan.controller.driver.SessionResource;
import com.friends.tanistan.service.DriverService;

@RestController
@RequestMapping(value = "/tanistan/driver/session", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SessionController {
	
	private final DriverService driverService;
	
	public SessionController(DriverService driverService) {
		this.driverService = driverService;
	}

	@GetMapping
	public ResponseEntity<SessionResource> getSession(@RequestBody SessionDto sessionDto) {
		return ResponseEntity.ok(driverService.getSession(sessionDto));
	}
	
	@DeleteMapping("/sessionId")
	public ResponseEntity<DeleteSessionResource> deleteSession(@PathVariable String sessionId) {
		return ResponseEntity.ok(driverService.deleteSession(sessionId));
	}
	
}
