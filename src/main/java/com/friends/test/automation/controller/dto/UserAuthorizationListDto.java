package com.friends.test.automation.controller.dto;

import java.util.List;

public class UserAuthorizationListDto {

    private List<String> authorizations;

    public List<String> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(List<String> authorizations) {
        this.authorizations = authorizations;
    }
}
