package com.friends.test.automation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
public class TestStep extends TanistanBaseEntity<String> {

    @Column(length = 4096)
    private String result;

    private Integer status;

    private boolean runStatus = false;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date executionTime;

    @ManyToOne
    private TestCaseInstanceRunner testCaseInstanceRunner;

    public boolean isRunStatus() {
        return runStatus;
    }

    public void setRunStatus(boolean runStatus) {
        this.runStatus = runStatus;
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public TestCaseInstanceRunner getTestCaseInstanceRunner() {
        return testCaseInstanceRunner;
    }

    public void setTestCaseInstanceRunner(TestCaseInstanceRunner testCaseInstanceRunner) {
        this.testCaseInstanceRunner = testCaseInstanceRunner;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
