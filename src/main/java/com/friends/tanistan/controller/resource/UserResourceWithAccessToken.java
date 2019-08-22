package com.friends.tanistan.controller.resource;

public class UserResourceWithAccessToken extends UserResource {

    private String accessToken;

    public UserResourceWithAccessToken(UserResource userResource) {
        super(userResource);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
