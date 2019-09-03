package com.friends.test.automation.controller;

import com.friends.test.automation.controller.dto.UserAuthorizationListDto;
import com.friends.test.automation.controller.dto.UserDto;
import com.friends.test.automation.controller.resource.UserResource;
import com.friends.test.automation.entity.UserAuthorization;
import com.friends.test.automation.entity.UserEntity;
import com.friends.test.automation.service.UserService;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tanistan/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

    private final UserService<UserEntity> userService;
    private final ConversionService conversionService;

    public UserController(UserService<UserEntity> userService, ConversionService conversionService) {
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @PostMapping
    @PreAuthorize(value = "hasAnyRole('ROLE_ROOT')")
    public ResponseEntity<UserResource> createUser(@RequestBody @Valid UserDto userDto) {
        UserEntity userEntity = conversionService.convert(userDto, UserEntity.class);
        UserResource userResource = conversionService.convert(userService.createUserEntity(userEntity,null),
                UserResource.class);
        return ResponseEntity.of(Optional.of(userResource));
    }

    @PostMapping("/company/{companyId}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_ROOT')")
    public ResponseEntity<UserResource> createUserUnderCompany(@RequestBody @Valid UserDto userDto,
            @PathVariable String companyId) {
        UserEntity userEntity = conversionService.convert(userDto, UserEntity.class);
        UserResource userResource = conversionService.convert(userService.createUserEntity(userEntity, companyId),
                UserResource.class);
        return ResponseEntity.of(Optional.of(userResource));
    }

    @GetMapping(value = "/all")
    @PreAuthorize(value = "hasAnyRole('ROLE_ROOT')")
    public ResponseEntity<Page<UserResource>> getUsers(@PageableDefault Pageable pageable) {
        Page<UserEntity> result = userService.getUsers(pageable);
        return ResponseEntity.of(Optional.of(result.map(item -> conversionService.convert(item, UserResource.class))));
    }


    @GetMapping(value = "/company/{companyId}/all")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_ROOT')")
    public ResponseEntity<Page<UserResource>> getUsersByCompanyId(@PathVariable String companyId,
            @PageableDefault Pageable pageable) {
        Page<UserEntity> result = userService.getUsersByCompanyId(companyId, pageable);
        return ResponseEntity.of(Optional.of(result.map(item -> conversionService.convert(item, UserResource.class))));
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_ROOT')")
    public ResponseEntity<UserResource> getUserById(@PathVariable String id) {
        UserEntity result = userService.getUserById(id);
        return ResponseEntity.of(Optional.of(conversionService.convert(result, UserResource.class)));
    }

    @PatchMapping(value = "/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_ROOT')")
    public ResponseEntity<UserResource> updateUser(@PathVariable String id, @RequestBody @Valid UserDto userDto) {
        UserEntity result = userService.updateUser(id, conversionService.convert(userDto, UserEntity.class));
        return ResponseEntity.of(Optional.of(conversionService.convert(result, UserResource.class)));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_ROOT')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();

    }

    @PostMapping(value = "/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_ROOT')")
    public ResponseEntity<UserResource> giveAuthorizationToUser(@PathVariable("id") String userId,
            @RequestBody UserAuthorizationListDto userAuthorizationListDto) {
        final List<UserAuthorization> userAuthorizationList = new ArrayList<>();
        userAuthorizationListDto.getAuthorizations()
                .forEach(auth -> userAuthorizationList.add(new UserAuthorization(auth)));

        UserEntity user = userService.givePermissionToUser(userId, userAuthorizationList);
        return ResponseEntity.ok(conversionService.convert(user, UserResource.class));
    }

}
