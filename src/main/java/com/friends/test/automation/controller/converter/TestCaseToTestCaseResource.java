package com.friends.test.automation.controller.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friends.test.automation.controller.driver.TestCaseResource;
import com.friends.test.automation.controller.resource.ErrorResource;
import com.friends.test.automation.entity.TestCase;
import com.friends.test.automation.entity.TestSuite;
import com.friends.test.automation.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.stream.Collectors;

public class TestCaseToTestCaseResource implements Converter<TestCase, TestCaseResource> {

    private Logger logger = LoggerFactory.getLogger(TestCaseToTestCaseResource.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public TestCaseResource convert(TestCase source) {
        TestCaseResource testCaseResource = new TestCaseResource();
        testCaseResource.setCreatedDate(source.getCreatedDate());
        testCaseResource.setId(source.getId());
        testCaseResource.setName(source.getName());
        if (!CollectionUtils.isEmpty(source.getTestSuite())) {
            testCaseResource
                    .setTestSuiteNames(
                            source.getTestSuite().stream().map(TestSuite::getName).collect(Collectors.toList()));
        }
        try {
            testCaseResource.setTestCommands(objectMapper.readValue(source.getTestCommands(), JsonNode.class));
        } catch (IOException e) {
            logger.error("Test Commands as string can not be converted to JsonNode");
            throw new BadRequestException(
                    ErrorResource.ErrorContent.builder()
                            .message("Test Commands as string can not be converted to JsonNode")
                            .build(""));
        }
        return testCaseResource;
    }
}
