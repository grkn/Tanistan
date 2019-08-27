package com.friends.test.automation.controller.dto;

import java.util.List;

public class TestSuiteDto {

    private String parentId;
    private String name;
    private List<TestCaseDto> testCase;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TestCaseDto> getTestCase() {
        return testCase;
    }

    public void setTestCase(List<TestCaseDto> testCase) {
        this.testCase = testCase;
    }
}
