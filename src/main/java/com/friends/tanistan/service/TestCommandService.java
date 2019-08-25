package com.friends.tanistan.service;

import com.friends.tanistan.entity.TestModel;
import com.friends.tanistan.entity.UserEntity;
import com.friends.tanistan.repository.TestCommandsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TestCommandService {

    private final TestCommandsRepository testCommandsRepository;
    private final UserService<UserEntity> userService;

    public TestCommandService(TestCommandsRepository testCommandsRepository,
            UserService<UserEntity> userService) {
        this.testCommandsRepository = testCommandsRepository;
        this.userService = userService;
    }

    public TestModel save(String userId, TestModel testModel) {
        testModel.setUserEntity(userService.getUserById(userId));
        return this.testCommandsRepository.save(testModel);
    }

    public Page<TestModel> findAllByUserId(String userId, Pageable pageable) {
        return this.testCommandsRepository.findAllByUserEntityId(userId, pageable);
    }
}
