package com.friends.test.automation.controller.converter;

import com.friends.test.automation.controller.resource.TestCaseResource;
import com.friends.test.automation.controller.resource.TestSuiteParentResource;
import com.friends.test.automation.controller.resource.TestSuiteResource;
import com.friends.test.automation.entity.TestCase;
import com.friends.test.automation.entity.TestSuite;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class TestSuiteToTestSuiteResource implements Converter<TestSuite, TestSuiteResource> {

    private final Converter<TestCase, TestCaseResource> converter;

    public TestSuiteToTestSuiteResource(
            Converter<TestCase, TestCaseResource> converter) {
        this.converter = converter;
    }


    @Override
    public TestSuiteResource convert(TestSuite source) {
        if (source == null) {
            return null;
        }
        TestSuiteResource testSuiteResource = new TestSuiteResource();
        testSuiteResource.setName(source.getName());
        testSuiteResource.setId(source.getId());
        if (isRootNode(source) || !isLeaf(source)) {
            List<TestSuiteResource> list = source.getChildren().stream().map(this::convert)
                    .collect(Collectors.toList());
            testSuiteResource.setChildren(list);
        }

        if (source.getParent() != null) {
            TestSuiteParentResource testSuiteParentResource = new TestSuiteParentResource();
            testSuiteParentResource.setId(source.getParent().getId());
            testSuiteParentResource.setName(source.getParent().getName());
            testSuiteResource.setParent(testSuiteParentResource);
        }

        testSuiteResource
                .setTestCases(source.getTestCases().stream().map(testCase -> converter.convert(testCase)).collect(
                        Collectors.toList()));

        return testSuiteResource;
    }

    private boolean isLeaf(TestSuite source) {
        return CollectionUtils.isEmpty(source.getChildren());
    }

    private boolean isRootNode(TestSuite source) {
        return source.getParent() == null || source.getParent().getId() == null;
    }
}
