package com.friends.tanistan.controller.driver;

import com.fasterxml.jackson.databind.JsonNode;

public class DefaultResource {

    private String sessionId;
    private int status;
    private JsonNode value;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public JsonNode getValue() {
        return value;
    }

    public void setValue(JsonNode value) {
        this.value = value;
    }
}
