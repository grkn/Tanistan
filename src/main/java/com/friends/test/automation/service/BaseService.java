package com.friends.test.automation.service;

import org.springframework.security.core.context.SecurityContextHolder;

abstract class BaseService {

    public String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication() == null ? "anyUser"
                : SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
