package com.friends.test.automation.tobedeleted;

import com.friends.test.automation.entity.TestSuite;
import com.friends.test.automation.entity.UserAuthorization;
import com.friends.test.automation.entity.UserEntity;
import com.friends.test.automation.repository.OAuthClientDetailsRepository;
import com.friends.test.automation.repository.TestSuiteRepository;
import com.friends.test.automation.repository.UserAuthorizationRepository;
import com.friends.test.automation.repository.UserRepository;
import com.friends.test.automation.util.SecurityUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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

    @Autowired
    private TestSuiteRepository testSuiteRepository;

    @Autowired
    private OAuthClientDetailsRepository clientDetailsRepository;

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

        if (userRepository.findByAccountNameOrEmailAddress("grkn", "grkn") == null) {

            UserEntity userEntity = new UserEntity();
            userEntity.setAccountName(clientId);
            userEntity.setAccountPhrase(encodedPassword);
            userEntity.setEmailAddress(clientId);
            userEntity = userRepository.save(userEntity);

            userEntity.setUserAuthorization(Sets.newHashSet());

            List<UserEntity> userList = Lists.newArrayList(userEntity);

            if (!userAuthorizationRepository.existsByAuthority("ROLE_ADMIN")) {
                UserAuthorization userAuth = new UserAuthorization();
                userAuth.setAuthority("ROLE_ADMIN");
                userAuth.setUserEntity(userList);
                userEntity.getUserAuthorization().add(userAuthorizationRepository.save(userAuth));
            } else {
                userEntity.getUserAuthorization().add(userAuthorizationRepository.findByAuthority("ROLE_ADMIN").get());
            }

            if (!userAuthorizationRepository.existsByAuthority("ROLE_USER")) {
                UserAuthorization userAuth2 = new UserAuthorization();
                userAuth2.setAuthority("ROLE_USER");
                userAuth2.setUserEntity(userList);
                userEntity.getUserAuthorization().add(userAuthorizationRepository.save(userAuth2));
            } else {
                userEntity.getUserAuthorization().add(userAuthorizationRepository.findByAuthority("ROLE_USER").get());
            }

            if (!userAuthorizationRepository.existsByAuthority("ROLE_ROOT")) {
                UserAuthorization userAuth2 = new UserAuthorization();
                userAuth2.setAuthority("ROLE_ROOT");
                userAuth2.setUserEntity(userList);
                userEntity.getUserAuthorization().add(userAuthorizationRepository.save(userAuth2));
            } else {
                userEntity.getUserAuthorization().add(userAuthorizationRepository.findByAuthority("ROLE_ROOT").get());
            }

            userRepository.save(userEntity);
            try {
                Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ROOT"));

                Authentication authentication = new UsernamePasswordAuthenticationToken(clientId,
                        isEncoded ? encodedPassword : password,
                        grantedAuthorities);
                defaultTokenServices
                        .createAccessToken(new OAuth2Authentication(SecurityUtil.prepareOAuth2Request(authentication),
                                authentication)).getValue();

                clientDetailsRepository
                        .insertAccessToken(clientId, isEncoded ? encodedPassword : password,
                                "ROLE_USER,ROLE_ADMIN,ROLE_ROOT",
                                900,
                                2592000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        TestSuite testSuite = new TestSuite();
        testSuite.setName("Root");
        testSuite.setParent(null);
        testSuite.setChildren(null);
        testSuite.setUserEntity(null);
        testSuite.setTestProject(null);
        testSuiteRepository.save(testSuite);
    }
}

