package com.friends.test.automation.service;

import com.friends.test.automation.controller.resource.ErrorResource;
import com.friends.test.automation.entity.TestCase;
import com.friends.test.automation.entity.TestProject;
import com.friends.test.automation.entity.TestSuite;
import com.friends.test.automation.entity.UserEntity;
import com.friends.test.automation.exception.NotFoundException;
import com.friends.test.automation.repository.TestCaseRepository;
import com.friends.test.automation.repository.TestProjectRepository;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TestProjectService extends BaseService {

    private final TestProjectRepository testProjectRepository;
    private final UserService<UserEntity> userService;
    private final TestCaseRepository testCaseRepository;
    private final TestSuiteService testSuiteService;

    public TestProjectService(TestProjectRepository testProjectRepository,
            UserService<UserEntity> userService,
            TestCaseRepository testCaseRepository,
            TestSuiteService testSuiteService) {
        this.testProjectRepository = testProjectRepository;
        this.userService = userService;
        this.testCaseRepository = testCaseRepository;
        this.testSuiteService = testSuiteService;
    }

    @Transactional
    public TestProject createOrUpdate(TestProject testProject) {
        if (!StringUtils.isEmpty(testProject.getId())) {
            String name = testProject.getName();
            testProject = testProjectRepository.findById(testProject.getId()).orElseThrow(() -> new NotFoundException(
                    ErrorResource.ErrorContent.builder().message("Project can not be found by given id").build("")));
            testProject.setName(name);
        } else {
            testProject.setUserEntities(getUserEntities(testProject));
            testProject.setTestCases(getTestCases(testProject));
            testProject.setTestSuites(getSuites(testProject));
        }
        return testProjectRepository.save(testProject);
    }

    private Set<UserEntity> getUserEntities(TestProject testProject) {
        return testProject.getUserEntities().stream()
                .map(userEntity -> {
                    UserEntity user = userService.getUserById(userEntity.getId());
                    List<TestProject> testProjectSet = user.getProjects();
                    if (CollectionUtils.isEmpty(testProjectSet)) {
                        user.setProjects(Lists.newArrayList());
                    }
                    user.getProjects().add(testProject);
                    return user;
                }).collect(
                        Collectors.toSet());
    }

    private Set<TestCase> getTestCases(TestProject testProject) {
        return testProject.getTestCases().stream().map(testCase -> {
            TestCase tcases = testCaseRepository.findById(testCase.getId()).orElseThrow(() -> new NotFoundException(
                    ErrorResource.ErrorContent.builder().message("Test case can not be found right now.").build("")));
            tcases.setTestProject(testProject);
            return tcases;
        }).collect(Collectors.toSet());
    }

    private Set<TestSuite> getSuites(TestProject testProject) {
        return testProject.getTestSuites().stream()
                .map(testSuite -> {
                    TestSuite suite = testSuiteService.getTestSuiteById(testSuite.getId());
                    suite.setTestProject(testProject);
                    return suite;
                }).collect(
                        Collectors.toSet());
    }

    public List<TestProject> findAll(String userId) {
        return testProjectRepository.findAllByUserEntitiesId(userId);
    }

    public void delete(String id) {
        this.testProjectRepository.deleteById(id);
    }

    public TestProject findById(String id, String userId) {
        return this.testProjectRepository.findByIdAndUserEntitiesId(id, userId).orElseThrow(() -> new NotFoundException(
                ErrorResource.ErrorContent.builder().message("Project can not be found by given user").build("")));
    }

    @Transactional
    public TestProject addAndRemove(String userId, String id, List<String> add, List<String> remove) {
        TestProject testProject = findById(id, userId);
        add.stream().forEach(added -> {
            UserEntity userEntity = userService.getUserById(added);
            userEntity.getProjects().add(testProject);
            testProject.getUserEntities().add(userEntity);
        });
        remove.stream().forEach(removed -> {
            UserEntity userEntity = userService.getUserById(removed);
            userEntity.getProjects().remove(testProject);
            testProject.getUserEntities().remove(userEntity);
        });
        return testProjectRepository.save(testProject);
    }
}
