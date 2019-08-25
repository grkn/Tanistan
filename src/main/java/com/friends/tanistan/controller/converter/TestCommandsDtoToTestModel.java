package com.friends.tanistan.controller.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friends.tanistan.controller.driver.TestCommandDto;
import com.friends.tanistan.controller.resource.ErrorResource;
import com.friends.tanistan.entity.TestModel;
import com.friends.tanistan.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class TestCommandsDtoToTestModel implements Converter<TestCommandDto, TestModel> {

    private Logger logger = LoggerFactory.getLogger(TestCommandsDtoToTestModel.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public TestModel convert(TestCommandDto source) {
        TestModel testModel = new TestModel();
        try {
            testModel.setTestCommands(objectMapper.writeValueAsString(source.getTestCommands()));
            testModel.setName(source.getName());
        } catch (JsonProcessingException e) {
            logger.error("Test Commands can not be converted to String");
            throw new BadRequestException(
                    ErrorResource.ErrorContent.builder().message("Test Commands can not be converted to String")
                            .build(""));
        }

        return testModel;
    }
}
