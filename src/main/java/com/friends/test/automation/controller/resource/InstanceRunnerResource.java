package com.friends.test.automation.controller.resource;

import java.util.Date;
import java.util.List;

public class InstanceRunnerResource {

    private String id;
    private String testCaseName;
    private Date startDate;
    private Date fisinedDate;
    private String userName;
    private boolean running;
    private List<StepResource> steps;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFisinedDate() {
        return fisinedDate;
    }

    public void setFisinedDate(Date fisinedDate) {
        this.fisinedDate = fisinedDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }


    public List<StepResource> getSteps() {
        return steps;
    }

    public void setSteps(List<StepResource> steps) {
        this.steps = steps;
    }
}
