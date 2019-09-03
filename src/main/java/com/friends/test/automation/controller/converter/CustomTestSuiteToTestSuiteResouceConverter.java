package com.friends.test.automation.controller.converter;

import com.friends.test.automation.controller.resource.TestSuiteParentResource;
import com.friends.test.automation.controller.resource.TestSuiteResource;
import com.friends.test.automation.entity.TestSuite;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class CustomTestSuiteToTestSuiteResouceConverter {

    private final TestCaseToTestCaseResource testCaseToTestCaseResource = new TestCaseToTestCaseResource();


    public TestSuiteResource convert(TestSuite source, String projectId) {
        if (source == null) {
            return null;
        }
        TestSuiteResource testSuiteResource = new TestSuiteResource();
        testSuiteResource.setName(source.getName());
        testSuiteResource.setId(source.getId());
        if (isRootNode(source) || !isLeaf(source)) {
            List<TestSuiteResource> list = source.getChildren().stream()
                    .filter(testSuite -> testSuite.getTestProject().getId().equals(projectId))
                    .map(testSuite -> convert(testSuite, projectId))
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
                .setTestCases(
                        source.getTestCases().stream().map(testCase -> testCaseToTestCaseResource.convert(testCase))
                                .collect(
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
