package com.friends.test.automation.controller.converter;

import com.friends.test.automation.controller.resource.TestProjectResource;
import com.friends.test.automation.entity.TestProject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

public class TestProjectToTestProjectResource implements Converter<TestProject, TestProjectResource> {

    private final TestCaseToTestCaseResource testCaseToTestCaseResource;
    private final TestSuiteToTestSuiteResource testSuiteToTestSuiteResource;
    private final UserToUserResourceConverter userToUserResourceConverter;

    public TestProjectToTestProjectResource(
            TestCaseToTestCaseResource testCaseToTestCaseResource,
            TestSuiteToTestSuiteResource testSuiteToTestSuiteResource,
            UserToUserResourceConverter userToUserResourceConverter) {
        this.testCaseToTestCaseResource = testCaseToTestCaseResource;
        this.testSuiteToTestSuiteResource = testSuiteToTestSuiteResource;
        this.userToUserResourceConverter = userToUserResourceConverter;
    }

    @Override
    public TestProjectResource convert(TestProject source) {
        TestProjectResource testProjectResource = new TestProjectResource();
        testProjectResource.setId(source.getId());
        testProjectResource.setName(source.getName());
        if (!CollectionUtils.isEmpty(source.getTestCases())) {
            testProjectResource.setTestCases(
                    source.getTestCases().stream().map(testCase -> testCaseToTestCaseResource.convert(testCase))
                            .collect(
                                    Collectors.toSet()));
        }

        if (!CollectionUtils.isEmpty(source.getTestSuites())) {
            testProjectResource.setTestSuites(
                    source.getTestSuites().stream().map(testSuite -> testSuiteToTestSuiteResource.convert(testSuite))
                            .collect(
                                    Collectors.toSet()));
        }

        if (!CollectionUtils.isEmpty(source.getUserEntities())) {
            testProjectResource.setUsers(
                    source.getUserEntities().stream().map(userEntity -> userToUserResourceConverter.convert(userEntity))
                            .collect(
                                    Collectors.toSet()));
        }

        return testProjectResource;
    }
}
