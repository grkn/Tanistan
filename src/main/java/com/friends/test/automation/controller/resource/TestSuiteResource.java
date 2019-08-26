package com.friends.test.automation.controller.resource;

import com.friends.test.automation.controller.driver.TestCaseResource;

import java.util.List;

public class TestSuiteResource {

    private String id;
    private List<TestSuiteResource> children;
    private String name;
    private TestSuiteParentResource parent;
    private List<TestCaseResource> testCases;

    public TestSuiteResource() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TestSuiteResource> getChildren() {
        return children;
    }

    public void setChildren(List<TestSuiteResource> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestSuiteParentResource getParent() {
        return parent;
    }

    public void setParent(TestSuiteParentResource parent) {
        this.parent = parent;
    }

    public List<TestCaseResource> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCaseResource> testCases) {
        this.testCases = testCases;
    }
}
