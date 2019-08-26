package com.friends.test.automation.config.security;

import com.friends.test.automation.entity.UserEntity;
import com.friends.test.automation.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private final UserService<UserEntity> userService;

    public CustomUserDetailService(UserService<UserEntity> userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userService.getUserByUsernameOrEmail(username, username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(String.format("Username or Email {0} not found", username));
        }

        Set<GrantedAuthority> grantedAuths = userEntity.getUserAuthorization().stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getAuthority())).collect(Collectors.toSet());

        return new User(userEntity.getEmailAddress(), userEntity.getAccountPhrase(), grantedAuths);

    }
}
