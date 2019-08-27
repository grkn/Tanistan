package com.friends.test.automation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class TestCase extends TanistanBaseEntity<String> {

    @Column(length = 4096 * 8)
    private String testCommands;

    private String name;

    @ManyToOne
    private UserEntity userEntity;

    @ManyToMany
    @JoinTable(name = "test_suite_test_case_rel",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "test_suite_id"))
    private Set<TestSuite> testSuite;

    @OneToMany(mappedBy = "testCase")
    private List<TestCaseInstanceRunner> testCaseInstanceRunners;

    public String getTestCommands() {
        return testCommands;
    }

    public void setTestCommands(String testCommands) {
        this.testCommands = testCommands;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TestSuite> getTestSuite() {
        return testSuite;
    }

    public void setTestSuite(Set<TestSuite> testSuite) {
        this.testSuite = testSuite;
    }

    public List<TestCaseInstanceRunner> getTestCaseInstanceRunners() {
        return testCaseInstanceRunners;
    }

    public void setTestCaseInstanceRunners(
            List<TestCaseInstanceRunner> testCaseInstanceRunners) {
        this.testCaseInstanceRunners = testCaseInstanceRunners;
    }
}
