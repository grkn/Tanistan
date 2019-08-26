package com.friends.test.automation.controller.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friends.test.automation.controller.driver.TestCaseDto;
import com.friends.test.automation.controller.resource.ErrorResource;
import com.friends.test.automation.entity.TestCase;
import com.friends.test.automation.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class TestCaseDtoToTestCase implements Converter<TestCaseDto, TestCase> {

    private Logger logger = LoggerFactory.getLogger(TestCaseDtoToTestCase.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public TestCase convert(TestCaseDto source) {
        TestCase testCase = new TestCase();
        try {
            testCase.setTestCommands(objectMapper.writeValueAsString(source.getTestCommands()));
            testCase.setName(source.getName());
            testCase.setId(source.getId());
        } catch (JsonProcessingException e) {
            logger.error("Test Commands can not be converted to String");
            throw new BadRequestException(
                    ErrorResource.ErrorContent.builder().message("Test Commands can not be converted to String")
                            .build(""));
        }

        return testCase;
    }
}
