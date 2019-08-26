package com.friends.test.automation.controller.resource;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Set;

public class UserResource {

    private String id;
    private String name;
    private String middleName;
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date birthDay;
    private String emailAddress;
    private String phoneNumber;
    private String secretQuestion;
    private String accountName;
    private Set<UserAuthorizationResource> userAuthorization;

    public UserResource(UserResource userResource) {
        this.id = userResource.id;
        this.name = userResource.name;
        this.middleName = userResource.middleName;
        this.lastName = userResource.lastName;
        this.birthDay = userResource.birthDay;
        this.emailAddress = userResource.emailAddress;
        this.phoneNumber = userResource.phoneNumber;
        this.secretQuestion = userResource.secretQuestion;
        this.accountName = userResource.accountName;
        this.userAuthorization = userResource.userAuthorization;
    }

    public UserResource() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSecretQuestion() {
        return secretQuestion;
    }

    public void setSecretQuestion(String secretQuestion) {
        this.secretQuestion = secretQuestion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<UserAuthorizationResource> getUserAuthorization() {
        return userAuthorization;
    }

    public void setUserAuthorization(Set<UserAuthorizationResource> userAuthorization) {
        this.userAuthorization = userAuthorization;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

}
