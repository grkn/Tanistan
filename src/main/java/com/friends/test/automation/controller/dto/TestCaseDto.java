package com.friends.test.automation.controller.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class TestCaseDto {

    private String id;
    private JsonNode testCommands;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonNode getTestCommands() {
        return testCommands;
    }

    public void setTestCommands(JsonNode testCommands) {
        this.testCommands = testCommands;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
