package com.friends.test.automation.controller.converter;

import com.friends.test.automation.controller.dto.TestProjectDto;
import com.friends.test.automation.entity.TestCase;
import com.friends.test.automation.entity.TestProject;
import com.friends.test.automation.entity.TestSuite;
import com.friends.test.automation.entity.UserEntity;
import com.google.common.collect.Sets;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.stream.Collectors;

public class TestProjectDtoToTestProject implements Converter<TestProjectDto, TestProject> {

    private final UserDtoToUserEntityConverter userDtoToUserEntityConverter;
    private final TestSuiteDtoToTestSuite testSuiteDtoToTestSuite;
    private final TestCaseDtoToTestCase testCaseDtoToTestCase;

    public TestProjectDtoToTestProject(
            UserDtoToUserEntityConverter userDtoToUserEntityConverter,
            TestSuiteDtoToTestSuite testSuiteDtoToTestSuite,
            TestCaseDtoToTestCase testCaseDtoToTestCase) {
        this.userDtoToUserEntityConverter = userDtoToUserEntityConverter;
        this.testSuiteDtoToTestSuite = testSuiteDtoToTestSuite;
        this.testCaseDtoToTestCase = testCaseDtoToTestCase;
    }

    @Override
    public TestProject convert(TestProjectDto source) {
        TestProject testProject = new TestProject();
        testProject.setName(source.getName());
        testProject.setId(source.getId());
        if (!CollectionUtils.isEmpty(source.getUsers())) {
            Set<UserEntity> userEntitySet = source.getUsers().stream()
                    .map(userDto -> userDtoToUserEntityConverter.convert(userDto)).collect(
                            Collectors.toSet());
            testProject.setUserEntities(userEntitySet);
        } else {
            testProject.setUserEntities(Sets.newHashSet());
        }
        if (!CollectionUtils.isEmpty(source.getTestCases())) {
            Set<TestCase> testCases = source.getTestCases().stream()
                    .map(testCaseDto -> testCaseDtoToTestCase.convert(testCaseDto))
                    .collect(Collectors.toSet());
            testProject.setTestCases(testCases);
        } else {
            testProject.setTestCases(Sets.newHashSet());
        }

        if (!CollectionUtils.isEmpty(source.getTestSuites())) {
            Set<TestSuite> testSuites = source.getTestSuites().stream()
                    .map(testSuiteDto -> testSuiteDtoToTestSuite.convert(testSuiteDto)).collect(
                            Collectors.toSet());
            testProject.setTestSuites(testSuites);
        } else {
            testProject.setTestSuites(Sets.newHashSet());
        }
        return testProject;
    }
}
