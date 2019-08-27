package com.friends.test.automation.command.runner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friends.test.automation.controller.dto.FindElementDto;
import com.friends.test.automation.controller.dto.NavigateDto;
import com.friends.test.automation.controller.dto.SendKeysDto;
import com.friends.test.automation.controller.dto.SessionDto;
import com.friends.test.automation.controller.resource.DefaultResource;
import com.friends.test.automation.controller.resource.RunnableResource;
import com.friends.test.automation.controller.resource.SessionResource;
import com.friends.test.automation.entity.TestCase;
import com.friends.test.automation.entity.TestCaseInstanceRunner;
import com.friends.test.automation.entity.TestStep;
import com.friends.test.automation.repository.TestCaseInstanceRunnerRepository;
import com.friends.test.automation.repository.TestStepRepository;
import com.friends.test.automation.service.DriverService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;


public class CommandRunner implements Runnable {

    private Logger logger = LoggerFactory.getLogger(CommandRunner.class);
    private final ObjectMapper objectMapper;
    private final DriverService driverService;
    private final TestCase testCase;
    private final TestCaseInstanceRunnerRepository testCaseInstanceRunnerRepository;
    private final TestStepRepository testStepRepository;

    public CommandRunner(ObjectMapper objectMapper, DriverService driverService,
            TestCase testCase, TestCaseInstanceRunnerRepository testCaseInstanceRunnerRepository,
            TestStepRepository testStepRepository) {
        this.objectMapper = objectMapper;
        this.driverService = driverService;
        this.testCase = testCase;
        this.testCaseInstanceRunnerRepository = testCaseInstanceRunnerRepository;
        this.testStepRepository = testStepRepository;
    }


    @Override
    public void run() {
        TestCaseInstanceRunner testCaseInstanceRunner = new TestCaseInstanceRunner();
        testCaseInstanceRunner.setTestCase(testCase);
        testCaseInstanceRunner.setRunning(true);
        testCaseInstanceRunner.setUserEntity(testCase.getUserEntity());
        testCaseInstanceRunner = this.testCaseInstanceRunnerRepository.saveAndFlush(testCaseInstanceRunner);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setDesiredCapabilities(new SessionDto.DesiredCapabilities());
        ResponseEntity<SessionResource> sessionResource = driverService.getSession(sessionDto);
        if (sessionResource.getBody() != null) {
            String sessionId = sessionResource.getBody().getSessionId();

            try {
                List<RunnableResource> commands = objectMapper
                        .readValue(testCase.getTestCommands(), new TypeReference<List<RunnableResource>>() {
                        });
                DefaultResource defaultResource;
                for (RunnableResource resource : commands) {
                    TestStep testStep = new TestStep();
                    testStep.setRunStatus(true);
                    testStep.setTestCaseInstanceRunner(testCaseInstanceRunner);
                    testStep = testStepRepository.saveAndFlush(testStep);
                    if (testCaseInstanceRunner.getTestSteps() != null) {
                        testCaseInstanceRunner.getTestSteps().add(testStep);
                    } else {
                        testCaseInstanceRunner.setTestSteps(Lists.newArrayList(testStep));
                    }
                    testCaseInstanceRunner = testCaseInstanceRunnerRepository.saveAndFlush(testCaseInstanceRunner);

                    long s = System.currentTimeMillis();
                    switch (resource.getType()) {
                        case "goToUrl":
                            NavigateDto navigateDto = new NavigateDto();
                            navigateDto.setUrl(resource.getNavigateUrl());
                            defaultResource = driverService.navigate(sessionId, navigateDto).getBody();
                            break;
                        case "click":
                            defaultResource = click(sessionId, resource);
                            break;
                        case "sendKey":
                            defaultResource = sendKeys(sessionId, resource);
                            break;
                        default:
                            return;
                    }
                    if (defaultResource != null && defaultResource.getStatus() != 0) {

                        testStep.setExecutionTime(new Date(System.currentTimeMillis() - s));
                        testStep.setRunStatus(false);
                        testStep.setStatus(defaultResource.getStatus());
                        testStep.setResult(objectMapper.writeValueAsString(defaultResource.getValue()));

                        break;
                    } else {
                        testStep.setExecutionTime(new Date(System.currentTimeMillis() - s));
                        testStep.setRunStatus(false);
                        testStep.setStatus(0);
                        testStep.setResult("{\"value\" : \"SUCCESS\"}");
                    }
                    testStep = testStepRepository.saveAndFlush(testStep);
                    if (testCaseInstanceRunner.getTestSteps() != null) {
                        testCaseInstanceRunner.getTestSteps().add(testStep);
                    } else {
                        testCaseInstanceRunner.setTestSteps(Lists.newArrayList(testStep));
                    }
                    testCaseInstanceRunner = testCaseInstanceRunnerRepository.saveAndFlush(testCaseInstanceRunner);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RuntimeException ex) {
                logger.error("Unexpected Error @CommandRunner", ex);
            } finally {
                driverService.deleteSession(sessionId);
                testCaseInstanceRunner.setRunning(false);
                testCaseInstanceRunnerRepository.saveAndFlush(testCaseInstanceRunner);
            }
        }

    }

    private DefaultResource click(String sessionId, RunnableResource resource) {
        DefaultResource defaultResource = findElement(sessionId, resource);
        if (defaultResource != null && defaultResource.getStatus() != 0) {
            defaultResource = driverService
                    .clickElement(sessionId, defaultResource.getValue().get("ELEMENT").textValue()).
                            getBody();
        }
        return defaultResource;
    }

    private DefaultResource findElement(String sessionId, RunnableResource resource) {
        DefaultResource defaultResource;
        FindElementDto findElementDto = new FindElementDto();
        findElementDto.setUsing(resource.getSelectionType());
        findElementDto.setValue(resource.getSelectionValue());
        defaultResource = driverService.findElement(sessionId, findElementDto).getBody();
        return defaultResource;
    }

    private DefaultResource sendKeys(String sessionId, RunnableResource resource) {
        DefaultResource defaultResource = findElement(sessionId, resource);
        if (defaultResource != null && defaultResource.getStatus() != 0) {
            SendKeysDto sendKeysDto = new SendKeysDto();
            sendKeysDto.setValue(Lists.newArrayList(resource.getMessage()));
            defaultResource = driverService
                    .sendKeys(sessionId, defaultResource.getValue().get("ELEMENT").textValue(),
                            sendKeysDto)
                    .getBody();
        }
        return defaultResource;
    }
}
