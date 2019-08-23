package com.friends.tanistan.tobedeleted;

import com.friends.tanistan.entity.UserAuthorization;
import com.friends.tanistan.entity.UserEntity;
import com.friends.tanistan.repository.UserAuthorizationRepository;
import com.friends.tanistan.repository.UserRepository;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * TO BE DELETED
 *
 * @author grkn
 */
@Component
public class InitializeUser {

    // INITIALIZE USER AND OAuth2 implementation JWT Token
    // TO BE DELETED
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthorizationRepository userAuthorizationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DefaultTokenServices defaultTokenServices;

    private final static String CLIENT_ID = "grkn";
    private final static String PASSWORD = "grkn";

    // TO BE DELETED
    @PostConstruct
    @Transactional
    public void setUp() {
        createPreDefinedUser(CLIENT_ID, PASSWORD, false);
    }

    public void createPreDefinedUser(String clientId, String password, boolean isEncoded) {
        String encodedPassword = passwordEncoder.encode(password);

        if (isEncoded) {
            System.out.println("******************************");
            System.out.println("User : " + clientId + " -> Encoded Password : " + encodedPassword);
            System.out.println("******************************");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setAccountName(clientId);
        userEntity.setAccountPhrase(encodedPassword);
        userEntity.setEmailAddress(clientId);
        userEntity = userRepository.save(userEntity);
        userEntity.setUserAuthorization(Sets.newHashSet());

        if (!userAuthorizationRepository.existsByAuthority("ROLE_ADMIN")) {
            UserAuthorization userAuth = new UserAuthorization();
            userAuth.setAuthority("ROLE_ADMIN");
            userAuth.setUserEntity(userEntity);
            userEntity.getUserAuthorization().add(userAuthorizationRepository.save(userAuth));
        } else {
            userEntity.getUserAuthorization().add(userAuthorizationRepository.findByAuthority("ROLE_ADMIN").get());
        }

        if (!userAuthorizationRepository.existsByAuthority("ROLE_USER")) {
            UserAuthorization userAuth2 = new UserAuthorization();
            userAuth2.setAuthority("ROLE_USER");
            userAuth2.setUserEntity(userEntity);
            userEntity.getUserAuthorization().add(userAuthorizationRepository.save(userAuth2));
        } else {
            userEntity.getUserAuthorization().add(userAuthorizationRepository.findByAuthority("ROLE_USER").get());
        }

        userRepository.save(userEntity);

        try {
            // For root 1 hour to accessToken, 1 year to refreshTokenValidity
            Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            Authentication authentication = new UsernamePasswordAuthenticationToken(clientId,
                    isEncoded ? encodedPassword : password,
                    grantedAuthorities);
            defaultTokenServices
                    .createAccessToken(new OAuth2Authentication(prepareOAuth2Request(authentication),
                            authentication)).getValue();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private OAuth2Request prepareOAuth2Request(Authentication authentication) {
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("client_id", authentication.getPrincipal().toString());
        requestParameters.put("client_secret", authentication.getCredentials().toString());

        String clientId = authentication.getPrincipal().toString();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Set<String> responseType = new HashSet<>();

        Set<String> scope = new HashSet<>();
        scope.add("write");
        scope.add("read");

        return new OAuth2Request(requestParameters, clientId, authorities, true, scope, null,
                null, responseType, null);
    }
}

