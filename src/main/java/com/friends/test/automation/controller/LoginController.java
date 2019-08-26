package com.friends.test.automation.controller;

import com.friends.test.automation.controller.dto.LoginUserDto;
import com.friends.test.automation.controller.resource.UserResource;
import com.friends.test.automation.entity.UserEntity;
import com.friends.test.automation.service.UserService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user/authenticate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LoginController {

    private final UserService<UserEntity> userService;
    private final PasswordEncoder passwordEncoder;
    private final ConversionService conversionService;

    public LoginController(UserService<UserEntity> userService,
            PasswordEncoder passwordEncoder, ConversionService conversionService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.conversionService = conversionService;
    }

    @PostMapping
    public ResponseEntity<UserResource> login(@RequestBody LoginUserDto userDto) {
        UserEntity userEntity = this.userService.getUserByUsernameOrEmail(userDto.getUsername(), userDto.getPassword());

        if (userEntity == null) {
            throw new BadCredentialsException("Username or Password not found.");
        }

        if (!passwordEncoder.matches(userDto.getPassword(), userEntity.getAccountPhrase())) {
            throw new BadCredentialsException("Username or Password not found.");
        }

        Set<GrantedAuthority> grantedAuths = userEntity.getUserAuthorization().stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getAuthority())).collect(Collectors.toSet());

        SecurityContextHolder.createEmptyContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userEntity.getEmailAddress(), userEntity.getAccountPhrase(),
                        grantedAuths));

        return ResponseEntity.ok(conversionService.convert(userEntity,UserResource.class));

    }
}
