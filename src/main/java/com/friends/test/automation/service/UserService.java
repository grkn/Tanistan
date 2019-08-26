package com.friends.test.automation.service;

import com.friends.test.automation.controller.resource.ErrorResource;
import com.friends.test.automation.entity.UserAuthorization;
import com.friends.test.automation.entity.UserEntity;
import com.friends.test.automation.exception.AlreadyExistsException;
import com.friends.test.automation.exception.NotFoundException;
import com.friends.test.automation.repository.OAuthClientDetailsRepository;
import com.friends.test.automation.repository.UserAuthorizationRepository;
import com.friends.test.automation.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService<T extends UserEntity> extends BaseService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthorizationRepository userAuthorizationRepository;
    private final OAuthClientDetailsRepository oAuthClientDetailsRepository;
    private static final String DEFAULT_AUTH = "ROLE_USER";

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            UserAuthorizationRepository userAuthorizationRepository,
            OAuthClientDetailsRepository oAuthClientDetailsRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAuthorizationRepository = userAuthorizationRepository;
        this.oAuthClientDetailsRepository = oAuthClientDetailsRepository;
    }

    @Transactional
    public UserEntity createUserEntity(UserEntity userEntity) {
        if (userRepository
                .existsByAccountNameOrEmailAddress(userEntity.getAccountName(), userEntity.getEmailAddress())) {
            throw new AlreadyExistsException(ErrorResource.ErrorContent.builder().message(
                    String.format("User is already exist by given email address: %s or account: %s",
                            userEntity.getAccountName(), userEntity.getEmailAddress())).build(""));
        }
        String password = userEntity.getAccountPhrase();

        userEntity.setAccountPhrase(passwordEncoder.encode(password));
        setDefaultAuthorization(userEntity);

        userEntity = userRepository.save(userEntity);
        setAccessToken(userEntity, password);
        return userEntity;
    }

    public Page<UserEntity> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public UserEntity getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorResource.ErrorContent.builder()
                .message(String.format("User can not be found by given id : %s", id)).build("")));
    }

    @Transactional
    public UserEntity updateUser(String id, UserEntity user) {
        UserEntity userEntity = getUserById(id);
        updateClientIdAndClientSecret(user, userEntity);
        overrideVariables(user, userEntity);
        return userRepository.save(userEntity);
    }


    @Transactional
    public UserEntity givePermissionToUser(String userId, List<UserAuthorization> userAuthorizations) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserAuthorization(new HashSet<>());

        userAuthorizations.forEach(
                userAuthorization -> {
                    Optional<UserAuthorization> userAuthorizationOptional = userAuthorizationRepository
                            .findByAuthority(userAuthorization.getAuthority());
                    if (!userAuthorizationOptional.isPresent()) {
                        throw new NotFoundException(ErrorResource.ErrorContent.builder().message(
                                String.format("Authorization can not be found by given name : %s",
                                        userAuthorization.getAuthority())).build(""));


                    } else {
                        userEntity.getUserAuthorization().add(userAuthorizationOptional.get());
                    }
                });

        return updateUser(userId, userEntity);
    }


    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public UserEntity getUserByUsernameOrEmail(String userName, String email) {
        return userRepository.findByAccountNameOrEmailAddress(userName, email);
    }

    private void overrideVariables(UserEntity user, UserEntity userEntity) {
        if (!StringUtils.isEmpty(user.getName())) {
            userEntity.setName(user.getName());
        }
        if (user.getBirthDay() != null) {
            userEntity.setBirthDay(user.getBirthDay());
        }
        if (!StringUtils.isEmpty(user.getEmailAddress())) {
            userEntity.setEmailAddress(user.getEmailAddress());
        }
        if (!StringUtils.isEmpty(user.getLastName())) {
            userEntity.setLastName(user.getLastName());
        }
        if (!StringUtils.isEmpty(user.getMiddleName())) {
            userEntity.setMiddleName(user.getMiddleName());
        }
        if (!StringUtils.isEmpty(user.getPhoneNumber())) {
            userEntity.setPhoneNumber(user.getPhoneNumber());
        }
        if (!StringUtils.isEmpty(user.getSecretQuestion())) {
            userEntity.setSecretQuestion(user.getSecretQuestion());
        }
        if (!CollectionUtils.isEmpty(user.getUserAuthorization())) {
            user.getUserAuthorization().forEach(userAuthorization -> {
                userAuthorization.setUserEntity(userEntity);
                userEntity.getUserAuthorization().add(userAuthorization);
            });

            List<String> auths = new ArrayList<>();
            userEntity.getUserAuthorization().forEach(userAuthorization -> auths.add(userAuthorization.getAuthority()));

            oAuthClientDetailsRepository.updateAuthorities(auths, userEntity.getEmailAddress());
        }
        if (!StringUtils.isEmpty(user.getSecretAnswer())) {
            userEntity.setSecretAnswer(user.getSecretAnswer());
        }
        if (user.getAttemptType() != null) {
            userEntity.setAttemptType(user.getAttemptType());
        }
        if (user.getLoginAttempt() != null) {
            userEntity.setLoginAttempt(user.getLoginAttempt());
        }
        if (user.getAccountPhrase() != null) {
            userEntity.setAccountPhrase(passwordEncoder.encode(user.getAccountPhrase()));
        }
        if (user.getAccountName() != null) {
            userEntity.setAccountName(user.getAccountName());
        }
    }

    private void setDefaultAuthorization(UserEntity userEntity) {
        UserAuthorization userAuthorization = userAuthorizationRepository.findByAuthority(DEFAULT_AUTH).get();
        userAuthorization.setUserEntity(userEntity);

        userEntity.setUserAuthorization(new HashSet<>());
        userEntity.getUserAuthorization().add(userAuthorizationRepository.save(userAuthorization));

    }

    private void setAccessToken(UserEntity userEntity, String password) {
        // 30 days to refreshToken, 15 min to accessToken
        oAuthClientDetailsRepository
                .insertAccessToken(StringUtils.isEmpty(userEntity.getAccountName()) ? userEntity.getEmailAddress()
                        : userEntity.getAccountName(), password, DEFAULT_AUTH, 900, 2592000);
    }

    private void updateClientIdAndClientSecret(UserEntity user, UserEntity userEntity) {
        String oldEmailAddress = userEntity.getEmailAddress();

        String clientId = userEntity.getEmailAddress();
        String clientSecret = userEntity.getAccountPhrase();
        if (!StringUtils.isEmpty(user.getEmailAddress())) {
            clientId = user.getEmailAddress();
        }
        if (!StringUtils.isEmpty(user.getAccountPhrase())) {
            clientSecret = user.getAccountPhrase();
        }

        oAuthClientDetailsRepository.updateClientIdAndClientSecret(clientId, clientSecret, oldEmailAddress, false);
    }

}
