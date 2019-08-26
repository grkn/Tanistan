package com.friends.test.automation.controller.converter;

import com.friends.test.automation.controller.driver.TestCaseDto;
import com.friends.test.automation.controller.driver.TestSuiteDto;
import com.friends.test.automation.entity.TestCase;
import com.friends.test.automation.entity.TestSuite;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestSuiteDtoToTestSuite implements Converter<TestSuiteDto, TestSuite> {

    private final Converter<TestCaseDto, TestCase> testCaseDtoToTestCaseConverter;

    public TestSuiteDtoToTestSuite(Converter<TestCaseDto, TestCase> testCaseDtoToTestCaseConverter1) {
        this.testCaseDtoToTestCaseConverter = testCaseDtoToTestCaseConverter1;
    }

    @Override
    public TestSuite convert(TestSuiteDto source) {
        TestSuite testSuite = new TestSuite();

        testSuite.setName(source.getName());

        TestSuite parent = new TestSuite();
        parent.setId(source.getParentId());
        testSuite.setParent(parent);

        Set<TestCase> set = source.getTestCase().stream()
                .map(testCaseDto -> testCaseDtoToTestCaseConverter.convert(testCaseDto)).collect(
                        Collectors.toSet());

        testSuite.setTestCases(set);
        return testSuite;
    }
}
