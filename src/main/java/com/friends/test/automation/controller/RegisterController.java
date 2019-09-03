package com.friends.test.automation.controller;

import com.friends.test.automation.controller.converter.GlobalConverter;
import com.friends.test.automation.controller.dto.CompanyDto;
import com.friends.test.automation.controller.resource.UserResource;
import com.friends.test.automation.entity.Company;
import com.friends.test.automation.entity.UserEntity;
import com.friends.test.automation.service.UserService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/register/userRegister", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RegisterController {

    private final UserService<UserEntity> userService;
    private final ConversionService conversionService;

    public RegisterController(
            UserService<UserEntity> userService, ConversionService conversionService) {
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @PostMapping
    public ResponseEntity<UserResource> registerUser(@RequestBody @Valid CompanyDto companyDto) {
        GlobalConverter globalConverter = new GlobalConverter();
        Company company = globalConverter.convert(companyDto, Company.class);
        globalConverter.clearVisitedList();
        UserEntity userEntity = userService.register(company);
        UserResource userResource = conversionService.convert(userEntity,
                UserResource.class);
        userResource.setCompanyId(userEntity.getCompany().getId());
        return ResponseEntity.of(Optional.of(userResource));
    }

}
