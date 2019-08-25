package com.friends.tanistan.controller.driver;

import com.fasterxml.jackson.databind.JsonNode;

public class TestCommandDto {

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
}
