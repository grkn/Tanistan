package com.friends.test.automation.controller.converter;

import com.friends.test.automation.controller.dto.TestCaseDto;
import com.friends.test.automation.controller.dto.TestSuiteDto;
import com.friends.test.automation.entity.TestCase;
import com.friends.test.automation.entity.TestSuite;
import com.google.common.collect.Sets;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

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

        if (!CollectionUtils.isEmpty(source.getTestCase())) {
            Set<TestCase> set = source.getTestCase().stream()
                    .map(testCaseDto -> testCaseDtoToTestCaseConverter.convert(testCaseDto)).collect(
                            Collectors.toSet());
            testSuite.setTestCases(set);
        } else {
            testSuite.setTestCases(Sets.newHashSet());
        }

        return testSuite;
    }
}
