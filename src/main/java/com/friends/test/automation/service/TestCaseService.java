package com.friends.test.automation.service;

import com.friends.test.automation.controller.resource.ErrorResource;
import com.friends.test.automation.entity.TestCase;
import com.friends.test.automation.entity.TestCaseInstanceRunner;
import com.friends.test.automation.entity.UserEntity;
import com.friends.test.automation.exception.AlreadyExistsException;
import com.friends.test.automation.exception.NotFoundException;
import com.friends.test.automation.repository.TestCaseInstanceRunnerRepository;
import com.friends.test.automation.repository.TestCaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final UserService<UserEntity> userService;
    private final TestCaseInstanceRunnerRepository testCaseInstanceRunnerRepository;

    public TestCaseService(TestCaseRepository testCaseRepository,
            UserService<UserEntity> userService,
            TestCaseInstanceRunnerRepository testCaseInstanceRunnerRepository) {
        this.testCaseRepository = testCaseRepository;
        this.userService = userService;
        this.testCaseInstanceRunnerRepository = testCaseInstanceRunnerRepository;
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

    public Page<TestCaseInstanceRunner> findAllInstanceRunnersByTestCaseId(String testCaseId, Pageable pageable) {
        return testCaseInstanceRunnerRepository.findAllByTestCaseId(testCaseId, pageable);
    }

    public TestCase findById(String id) {
        return testCaseRepository.findById(id).orElseThrow(() -> new NotFoundException(
                ErrorResource.ErrorContent.builder().message("Test case can not be found right now.").build("")));
    }
}
