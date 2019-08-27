package com.friends.test.automation.controller.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friends.test.automation.controller.resource.StepResource;
import com.friends.test.automation.entity.TestStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;

public class TestCaseStepToStepResource implements Converter<TestStep, StepResource> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(TestCaseStepToStepResource.class);

    @Override
    public StepResource convert(TestStep source) {
        StepResource stepResource = new StepResource();
        stepResource.setExecutionTime(source.getExecutionTime());
        try {
            if (source.getResult() != null) {
                stepResource.setResult(objectMapper.readValue(source.getResult(), JsonNode.class));
            }
        } catch (IOException e) {
            source.setResult("{\"value\" : \"RUNNING\"}");
            logger.error("Test Step result conversion to JSONNode error", e);
        }
        stepResource.setRunStatus(source.isRunStatus());
        stepResource.setStatus(source.getStatus());
        return stepResource;
    }
}
