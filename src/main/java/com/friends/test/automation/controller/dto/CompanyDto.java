package com.friends.test.automation.controller.dto;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class CompanyDto {

    @NotBlank(message = "Company name can not be empty")
    private String name;
    private List<UserDto> users;

    public CompanyDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}
