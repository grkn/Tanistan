package com.friends.test.automation.repository;

import com.friends.test.automation.entity.UserEntity;

public interface UserRepository extends BaseTanistanJpaRepository<UserEntity> {

	UserEntity findByAccountNameOrEmailAddress(String userName,String email);

	boolean existsByAccountNameOrEmailAddress(String userName,String email);

}
