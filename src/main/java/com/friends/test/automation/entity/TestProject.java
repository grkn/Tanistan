package com.friends.test.automation.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class TestProject extends TanistanBaseEntity<String> {

    private String name;
    @ManyToMany(mappedBy = "projects", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Column(name = "project_id")
    private Set<UserEntity> userEntities;

    @OneToMany(mappedBy = "testProject")
    private Set<TestSuite> testSuites;

    @OneToMany(mappedBy = "testProject")
    private Set<TestCase> testCases;

    public TestProject() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TestSuite> getTestSuites() {
        return testSuites;
    }

    public void setTestSuites(Set<TestSuite> testSuites) {
        this.testSuites = testSuites;
    }

    public Set<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(Set<TestCase> testCases) {
        this.testCases = testCases;
    }

    public Set<UserEntity> getUserEntities() {
        return userEntities;
    }

    public void setUserEntities(Set<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }
}
