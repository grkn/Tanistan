package com.friends.test.automation.repository;

import com.friends.test.automation.entity.UserAuthorization;

import java.util.Optional;

public interface UserAuthorizationRepository extends BaseTanistanJpaRepository<UserAuthorization> {

    Optional<UserAuthorization> findByAuthority(String authority);

    boolean existsByAuthority(String authority);
}
