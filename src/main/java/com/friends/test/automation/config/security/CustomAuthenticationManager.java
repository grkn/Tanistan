package com.friends.test.automation.config.security;

import com.friends.test.automation.entity.UserEntity;
import com.friends.test.automation.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationManager extends ProviderManager {

    private final UserService<UserEntity> userService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationManager(UserService<UserEntity> userService, PasswordEncoder passwordEncoder,
            CustomAuthenticationProvider customAuthenticationProvider) {
        super(Arrays.asList(customAuthenticationProvider));
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        UserEntity userEntity = this.userService.getUserByUsernameOrEmail(username, username);

        if (userEntity == null) {
            throw new BadCredentialsException("Username or Password not found.");
        }

        if (!passwordEncoder.matches(password, userEntity.getAccountPhrase())) {
            throw new BadCredentialsException("Username or Password not found.");
        }

        Set<GrantedAuthority> grantedAuths = userEntity.getUserAuthorization().stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getAuthority())).collect(Collectors.toSet());

        return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
    }
}