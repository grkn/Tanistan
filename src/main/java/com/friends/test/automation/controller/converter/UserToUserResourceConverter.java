package com.friends.test.automation.controller.converter;

import com.friends.test.automation.controller.resource.UserAuthorizationResource;
import com.friends.test.automation.controller.resource.UserResource;
import com.friends.test.automation.entity.UserEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

public class UserToUserResourceConverter implements Converter<UserEntity, UserResource> {

    @Override
    public UserResource convert(UserEntity source) {
        UserResource userResource = new UserResource();
        userResource.setId(source.getId());
        userResource.setName(source.getName());
        userResource.setBirthDay(source.getBirthDay());
        userResource.setEmailAddress(source.getEmailAddress());
        userResource.setLastName(source.getLastName());
        userResource.setMiddleName(source.getMiddleName());
        userResource.setPhoneNumber(source.getPhoneNumber());
        userResource.setSecretQuestion(source.getSecretQuestion());
        userResource.setAccountName(source.getAccountName());
        if (!CollectionUtils.isEmpty(source.getUserAuthorization())) {
            userResource.setUserAuthorization(source.getUserAuthorization().stream().map(uAuth -> {
                UserAuthorizationResource userAuthorizationResource = new UserAuthorizationResource();
                userAuthorizationResource.setAuthorization(uAuth.getAuthority());
                userAuthorizationResource.setId(uAuth.getId());
                return userAuthorizationResource;
            }).collect(Collectors.toSet()));
        }
        return userResource;
    }

}
