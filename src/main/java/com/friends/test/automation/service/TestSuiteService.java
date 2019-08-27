package com.friends.test.automation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friends.test.automation.command.runner.CommandRunner;
import com.friends.test.automation.controller.resource.ErrorResource;
import com.friends.test.automation.entity.TestCase;
import com.friends.test.automation.entity.TestSuite;
import com.friends.test.automation.exception.NotFoundException;
import com.friends.test.automation.repository.TestCaseInstanceRunnerRepository;
import com.friends.test.automation.repository.TestCaseRepository;
import com.friends.test.automation.repository.TestStepRepository;
import com.friends.test.automation.repository.TestSuiteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class TestSuiteService {

    private final TestSuiteRepository testSuiteRepository;
    private final TestCaseRepository testCaseRepository;
    private final DriverService driverService;
    private final TestCaseInstanceRunnerRepository testCaseInstanceRunnerRepository;
    private final TestStepRepository testStepRepository;
    private final ObjectMapper objectMapper;

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20,
            0L, TimeUnit.MILLISECONDS,
            new SynchronousQueue<>());

    public TestSuiteService(TestSuiteRepository testSuiteRepository,
            TestCaseRepository testCaseRepository, DriverService driverService,
            TestCaseInstanceRunnerRepository testCaseInstanceRunnerRepository,
            TestStepRepository testStepRepository, ObjectMapper objectMapper) {
        this.testSuiteRepository = testSuiteRepository;
        this.testCaseRepository = testCaseRepository;
        this.driverService = driverService;
        this.testCaseInstanceRunnerRepository = testCaseInstanceRunnerRepository;
        this.testStepRepository = testStepRepository;
        this.objectMapper = objectMapper;
    }

    public TestSuite createTestSuite(TestSuite testSuite) {
        TestSuite parent = checkIfParentExists(testSuite.getParent().getId());
        testSuite.setParent(parent);
        Set<TestCase> testCaseSet = testSuite.getTestCases();
        testCaseSet.forEach(testCase -> {
            TestCase test = this.testCaseRepository.findById(testCase.getId()).orElseThrow(() -> new NotFoundException(
                    ErrorResource.ErrorContent.builder().message("Test case can not be found").build("")));
            testSuite.getTestCases().add(test);
            test.getTestSuite().add(testSuite);
        });

        return testSuiteRepository.save(testSuite);
    }

    private TestSuite checkIfParentExists(String parentId) {
        return testSuiteRepository.findById(parentId).orElseThrow(() -> new NotFoundException(
                ErrorResource.ErrorContent.builder().message("Parent test suite can not be found").build("")));
    }

    public TestSuite getChildren(String parentId) {
        return testSuiteRepository.findById(parentId).orElseThrow(() -> new NotFoundException(
                ErrorResource.ErrorContent.builder().message("Parent can not be found").build("")));
    }

    public TestSuite getTestSuiteById(String id) {
        return testSuiteRepository.findById(id).orElseThrow(() -> new NotFoundException(
                ErrorResource.ErrorContent.builder().message("Suites can not be found").build("")));
    }

    public TestSuite getRoot() {
        return testSuiteRepository.findByParentIsNull();
    }

    @Transactional
    public TestSuite addTestCaseToTestSuite(String suiteId, List<String> testIds) {
        TestSuite testSuite = testSuiteRepository.findById(suiteId).orElseThrow(() -> new NotFoundException(
                ErrorResource.ErrorContent.builder().message("Test Suite can not be found.").build("")));
        for (String testCaseId : testIds) {
            TestCase testCase = testCaseRepository.findById(testCaseId).orElseThrow(() ->
                    new NotFoundException(
                            ErrorResource.ErrorContent.builder()
                                    .message(String.format("Test Case can not be found. Id : %s ", testCaseId))
                                    .build(""))
            );
            testCase.getTestSuite().add(testSuite);
            testSuite.getTestCases().add(testCase);

        }
        return testSuiteRepository.saveAndFlush(testSuite);
    }

    public List<TestCase> getTestCasesBySuiteIdAndUserId(String suiteId, String userId) {
        return testCaseRepository.findAllByUserEntityIdAndTestSuiteIdOrderByCreatedDateDesc(userId, suiteId);
    }

    @Transactional
    public void deleteTestCase(String suiteId, String testCaseId) {
        TestSuite testSuite = this.testSuiteRepository.findById(suiteId).orElseThrow(() -> new NotFoundException(
                ErrorResource.ErrorContent.builder().message("Test suite can not be found").build("")));
        List<TestCase> removeList = new ArrayList<>();
        testSuite.getTestCases().forEach(testCase -> {
            if (testCaseId.equals(testCase.getId())) {
                removeList.add(testCase);
                testCase.getTestSuite().remove(testSuite);
            }
        });
        testSuite.getTestCases().removeAll(removeList);
        testSuiteRepository.saveAndFlush(testSuite);
    }

    public void runTestCase(String suiteId) {
        Set<TestCase> testCases = testSuiteRepository.findById(suiteId).orElseThrow(() -> new NotFoundException(
                ErrorResource.ErrorContent.builder().message("TestSuite can not be found so can not run test cases")
                        .build(""))).getTestCases();
        for (TestCase testCase : testCases) {
            threadPoolExecutor.submit(new CommandRunner(objectMapper, driverService, testCase,
                    testCaseInstanceRunnerRepository, testStepRepository));
        }
    }
}
