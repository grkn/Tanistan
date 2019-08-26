package com.friends.test.automation.service;

import com.friends.test.automation.entity.UserAuthorization;
import com.friends.test.automation.repository.UserAuthorizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAuthorizationService extends BaseService {

    private final UserAuthorizationRepository userAuthorizationRepository;

    private Logger logger = LoggerFactory.getLogger(UserAuthorizationService.class);

    public UserAuthorizationService(UserAuthorizationRepository userAuthorizationRepository) {
        this.userAuthorizationRepository = userAuthorizationRepository;
    }

    public List<UserAuthorization> createAuthorizations(List<UserAuthorization> authorizations) {
        List<UserAuthorization> addedAuthorizations = new ArrayList<>();
        for (UserAuthorization userAuthorization : authorizations) {
            try {
                UserAuthorization savedAuthorization = userAuthorizationRepository.save(userAuthorization);
                addedAuthorizations.add(savedAuthorization);
            } catch (DataIntegrityViolationException ex) {
                logger.warn(
                        String.format("Authorization Unique Constraint exception. Authority : %s", userAuthorization),
                        ex);
            }
        }
        return addedAuthorizations;
    }


    public Page<UserAuthorization> findAll(Pageable pageable) {
        return userAuthorizationRepository.findAll(pageable);
    }

    protected void saveAuthorization(UserAuthorization userAuthorization) {
        userAuthorizationRepository.save(userAuthorization);
    }
}
