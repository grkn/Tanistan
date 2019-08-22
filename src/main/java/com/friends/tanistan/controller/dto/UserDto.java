package com.friends.tanistan.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Set;


/**
 * (?=.*[0-9]) a digit must occur at least once
 * (?=.*[a-z]) a lower case letter must occur at least once
 * (?=.*[A-Z]) an upper case letter must occur at least once
 * (?=.*[@#$%^&+=]) a special character must occur at least once
 * (?=\\S+$) no whitespace allowed in the entire string
 * .{8,} at least 8 characters
 */
public class UserDto {

    @NotBlank
    private String name;
    private String middleName;
    @NotBlank
    private String lastName;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date birthDay;
    @Email
    private String emailAddress;
    private String phoneNumber;
    private String secretQuestion;
    private String secretAnswer;
    private String accountName;
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
//            , message = "Password must be at least 8 characters. Also Password must contain at least one special, one lowercase character and one special character.")
    private String accountPhrase;
    private Set<UserAuthorizationDto> userAuthorzation;
    private String attemptType;
    private Integer loginAttempt;

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

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public String getAccountPhrase() {
        return accountPhrase;
    }

    public void setAccountPhrase(String accountPhrase) {
        this.accountPhrase = accountPhrase;
    }

    public String getAttemptType() {
        return attemptType;
    }

    public void setAttemptType(String attemptType) {
        this.attemptType = attemptType;
    }

    public Integer getLoginAttempt() {
        return loginAttempt;
    }

    public void setLoginAttempt(Integer loginAttempt) {
        this.loginAttempt = loginAttempt;
    }

    public Set<UserAuthorizationDto> getUserAuthorzation() {
        return userAuthorzation;
    }

    public void setUserAuthorzation(Set<UserAuthorizationDto> userAuthorzation) {
        this.userAuthorzation = userAuthorzation;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

}
