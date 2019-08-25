package com.friends.tanistan.controller.driver;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Date;

public class TestCommandsResource {
    private String id;
    private String name;
    private Date createdDate;
    private JsonNode testCommands;
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public JsonNode getTestCommands() {
        return testCommands;
    }

    public void setTestCommands(JsonNode testCommands) {
        this.testCommands = testCommands;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
