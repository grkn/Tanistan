package com.friends.tanistan.controller;

import com.friends.tanistan.controller.dto.UserAuthorizationListDto;
import com.friends.tanistan.controller.resource.UserAuthorizationResource;
import com.friends.tanistan.controller.resource.UserResource;
import com.friends.tanistan.entity.UserAuthorization;
import com.friends.tanistan.entity.UserEntity;
import com.friends.tanistan.service.UserAuthorizationService;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/tanistan/auhtorization", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AuthorizationController {

    private final UserAuthorizationService userAuthorizationService;
    private final ConversionService conversionService;

    public AuthorizationController(UserAuthorizationService userAuthorizationService,
            ConversionService conversionService) {
        this.userAuthorizationService = userAuthorizationService;
        this.conversionService = conversionService;
    }
    

    @PostMapping
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserAuthorizationResource>> createAuthorizationBulk(
            @RequestBody UserAuthorizationListDto userAuthorizationListDto) {
        final List<UserAuthorization> userAuthorizationList = new ArrayList<>();
        final List<UserAuthorizationResource> userAuthorizationResourceList = new ArrayList<>();

        userAuthorizationListDto.getAuthorizations()
                .forEach(auth -> userAuthorizationList.add(new UserAuthorization(auth)));

        List<UserAuthorization> insertedAuthorizations = userAuthorizationService
                .createAuthorizations(userAuthorizationList);

        insertedAuthorizations.forEach(userAuthorization -> userAuthorizationResourceList
                .add(conversionService.convert(userAuthorization, UserAuthorizationResource.class)));

        return ResponseEntity.ok(userAuthorizationResourceList);

    }

    @GetMapping("/all")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Page<UserAuthorizationResource>> findAllAuthorizations(@PageableDefault Pageable pageable) {
        Page<UserAuthorization> userAuthorizationPage = userAuthorizationService.findAll(pageable);
        final List<UserAuthorizationResource> userAuthorizationResourceList = new ArrayList<>();
        userAuthorizationPage
                .forEach(userAuthorization -> userAuthorizationResourceList
                        .add(conversionService.convert(userAuthorization, UserAuthorizationResource.class)));

        final Page<UserAuthorizationResource> userAuthorizationResourcePage = new PageImpl<>(
                userAuthorizationResourceList,
                userAuthorizationPage.getPageable(), userAuthorizationPage.getTotalElements());
        return ResponseEntity.ok(userAuthorizationResourcePage);
    }

}
