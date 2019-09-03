package com.friends.test.automation.repository;

import com.friends.test.automation.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository extends BaseTanistanJpaRepository<UserEntity> {

    UserEntity findByAccountNameOrEmailAddress(String userName, String email);

    boolean existsByAccountNameOrEmailAddress(String userName, String email);

    Page<UserEntity> findAllByCompanyId(String companyId, Pageable pageable);

}
