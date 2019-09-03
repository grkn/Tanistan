package com.friends.test.automation.controller.dto;

import javax.validation.constraints.NotBlank;
import java.util.Set;

public class TestProjectDto {


    private String id;
    @NotBlank
    private String name;
    private Set<UserDto> users;
    private Set<TestSuiteDto> testSuites;
    private Set<TestCaseDto> testCases;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserDto> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDto> users) {
        this.users = users;
    }

    public Set<TestSuiteDto> getTestSuites() {
        return testSuites;
    }

    public void setTestSuites(Set<TestSuiteDto> testSuites) {
        this.testSuites = testSuites;
    }

    public Set<TestCaseDto> getTestCases() {
        return testCases;
    }

    public void setTestCases(Set<TestCaseDto> testCases) {
        this.testCases = testCases;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
