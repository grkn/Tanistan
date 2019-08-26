package com.friends.test.automation.service;

import com.friends.test.automation.controller.resource.ErrorResource;
import com.friends.test.automation.entity.TestCase;
import com.friends.test.automation.entity.UserEntity;
import com.friends.test.automation.exception.AlreadyExistsException;
import com.friends.test.automation.repository.TestCaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final UserService<UserEntity> userService;

    public TestCaseService(TestCaseRepository testCaseRepository,
            UserService<UserEntity> userService) {
        this.testCaseRepository = testCaseRepository;
        this.userService = userService;
    }

    public TestCase save(String userId, TestCase testCase) {
        testCase.setUserEntity(userService.getUserById(userId));
        if (testCase.getId() == null) {
            checkTestCaseExistWithSameName(testCase.getName());
        }
        return this.testCaseRepository.save(testCase);
    }

    private void checkTestCaseExistWithSameName(String name) {
        testCaseRepository.existsByName(name).ifPresent((exists) -> {
            if (exists) {
                throw new AlreadyExistsException(
                        ErrorResource.ErrorContent.builder()
                                .message(String.format("Test Case exist with name : %s", name)).build(""));
            }
        });
    }

    public Page<TestCase> findAllByUserId(String userId, Pageable pageable) {
        return this.testCaseRepository.findAllByUserEntityId(userId, pageable);
    }
}
