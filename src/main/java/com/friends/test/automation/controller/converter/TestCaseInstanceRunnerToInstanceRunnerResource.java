package com.friends.test.automation.controller.converter;

import com.friends.test.automation.controller.resource.InstanceRunnerResource;
import com.friends.test.automation.entity.TestCaseInstanceRunner;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

public class TestCaseInstanceRunnerToInstanceRunnerResource implements
        Converter<TestCaseInstanceRunner, InstanceRunnerResource> {

    private final TestCaseStepToStepResource testCaseStepToStepResource;

    public TestCaseInstanceRunnerToInstanceRunnerResource(
            TestCaseStepToStepResource testCaseStepToStepResource) {
        this.testCaseStepToStepResource = testCaseStepToStepResource;
    }

    @Override
    public InstanceRunnerResource convert(TestCaseInstanceRunner source) {
        InstanceRunnerResource instanceRunnerResource = new InstanceRunnerResource();
        if (source.getTestCase() != null) {
            instanceRunnerResource.setTestCaseName(source.getTestCase().getName());
        }
        instanceRunnerResource.setFisinedDate(source.getModifiedDate());
        instanceRunnerResource.setId(source.getId());
        instanceRunnerResource.setRunning(source.isRunning());
        instanceRunnerResource.setStartDate(source.getCreatedDate());

        if (source.getUserEntity() != null) {
            instanceRunnerResource.setUserName(source.getUserEntity().getEmailAddress());
        }

        if (!CollectionUtils.isEmpty(source.getTestSteps())) {
            instanceRunnerResource
                    .setSteps(source.getTestSteps().stream().map(testCaseStepToStepResource::convert).collect(
                            Collectors.toList()));
        }

        return instanceRunnerResource;
    }
}
