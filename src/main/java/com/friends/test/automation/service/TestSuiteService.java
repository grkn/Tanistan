package com.friends.test.automation.service;

import com.friends.test.automation.controller.resource.ErrorResource;
import com.friends.test.automation.entity.TestCase;
import com.friends.test.automation.entity.TestSuite;
import com.friends.test.automation.exception.NotFoundException;
import com.friends.test.automation.repository.TestCaseRepository;
import com.friends.test.automation.repository.TestSuiteRepository;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class TestSuiteService {

    private final TestSuiteRepository testSuiteRepository;
    private final TestCaseRepository testCaseRepository;

    public TestSuiteService(TestSuiteRepository testSuiteRepository,
            TestCaseRepository testCaseRepository) {
        this.testSuiteRepository = testSuiteRepository;
        this.testCaseRepository = testCaseRepository;
    }

    public TestSuite createTestSuite(TestSuite testSuite) {
        TestSuite parent = checkIfParentExists(testSuite.getParent().getId());
        testSuite.setParent(parent);
        Set<TestCase> testCaseSet = testSuite.getTestCases();
        testCaseSet.forEach(testCase -> {
            TestCase test = this.testCaseRepository.findById(testCase.getId()).orElseThrow(() -> new NotFoundException(
                    ErrorResource.ErrorContent.builder().message("Test case can not be found").build("")));
            testSuite.getTestCases().add(test);
            test.setTestSuite(Sets.newHashSet(testSuite));
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

    public List<TestCase> getTestCasesBySuiteId(String suiteId) {
        return testCaseRepository.findAllByTestSuiteId(suiteId);
    }

    public void deleteTestCase(String suiteId, String testCaseId) {
        TestSuite testSuite = this.testSuiteRepository.findById(suiteId).orElseThrow(() -> new NotFoundException(
                ErrorResource.ErrorContent.builder().message("Test suite can not be found").build("")));
        testSuite.getTestCases().removeIf(element -> element.getId().equals(testCaseId));
        testSuiteRepository.save(testSuite);
    }
}
