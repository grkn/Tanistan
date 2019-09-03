package com.friends.test.automation.controller.resource;

import java.util.Set;

public class TestProjectResource {

    private String id;
    private String name;
    private Set<UserResource> users;
    private Set<TestCaseResource> testCases;
    private Set<TestSuiteResource> testSuites;

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

    public Set<UserResource> getUsers() {
        return users;
    }

    public void setUsers(Set<UserResource> users) {
        this.users = users;
    }

    public Set<TestCaseResource> getTestCases() {
        return testCases;
    }

    public void setTestCases(Set<TestCaseResource> testCases) {
        this.testCases = testCases;
    }

    public Set<TestSuiteResource> getTestSuites() {
        return testSuites;
    }

    public void setTestSuites(Set<TestSuiteResource> testSuites) {
        this.testSuites = testSuites;
    }
}
