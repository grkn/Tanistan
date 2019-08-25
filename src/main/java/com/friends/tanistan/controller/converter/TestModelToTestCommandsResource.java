package com.friends.tanistan.controller.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friends.tanistan.controller.driver.TestCommandsResource;
import com.friends.tanistan.controller.resource.ErrorResource;
import com.friends.tanistan.entity.TestModel;
import com.friends.tanistan.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;

public class TestModelToTestCommandsResource implements Converter<TestModel, TestCommandsResource> {

    private Logger logger = LoggerFactory.getLogger(TestModelToTestCommandsResource.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public TestCommandsResource convert(TestModel source) {
        TestCommandsResource testCommandsResource = new TestCommandsResource();
        testCommandsResource.setCreatedDate(source.getCreatedDate());
        testCommandsResource.setId(source.getId());
        testCommandsResource.setName(source.getName());
        try {
            testCommandsResource.setTestCommands(objectMapper.readValue(source.getTestCommands(), JsonNode.class));
        } catch (IOException e) {
            logger.error("Test Commands as string can not be converted to JsonNode");
            throw new BadRequestException(
                    ErrorResource.ErrorContent.builder()
                            .message("Test Commands as string can not be converted to JsonNode")
                            .build(""));
        }
        return testCommandsResource;
    }
}
