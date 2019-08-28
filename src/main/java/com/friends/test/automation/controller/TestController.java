package com.friends.test.automation.controller;

import com.friends.test.automation.controller.dto.TestCaseDto;
import com.friends.test.automation.controller.resource.InstanceRunnerResource;
import com.friends.test.automation.controller.resource.TestCaseResource;
import com.friends.test.automation.entity.TestCase;
import com.friends.test.automation.entity.TestCaseInstanceRunner;
import com.friends.test.automation.service.TestCaseService;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/tanistan/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TestController {

    private final TestCaseService testCaseService;
    private final ConversionService conversionService;

    public TestController(TestCaseService testCaseService,
            ConversionService conversionService) {
        this.testCaseService = testCaseService;
        this.conversionService = conversionService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<TestCaseResource> createTestCommands(@PathVariable String userId, @RequestBody
            TestCaseDto testCaseDto) {
        TestCase testCase = this.testCaseService
                .save(userId, conversionService.convert(testCaseDto, TestCase.class));
        TestCaseResource testCaseResource = conversionService.convert(testCase, TestCaseResource.class);
        return ResponseEntity.ok(testCaseResource);
    }

    @GetMapping("/user/{userId}/all")
    public ResponseEntity<Page<TestCaseResource>> findAllTestCommandsForUser(@PathVariable String userId,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TestCase> testModelPage = this.testCaseService.findAllByUserId(userId, pageable);
        List<TestCaseResource> testCaseResourceSet = testModelPage.get()
                .map(testModel -> conversionService.convert(testModel, TestCaseResource.class)).collect(
                        Collectors.toList());
        return ResponseEntity.ok(new PageImpl(testCaseResourceSet, pageable, testModelPage.getTotalElements()));
    }

    @GetMapping("/{testCaseId}/instancerunner/all")
    public ResponseEntity<Page<InstanceRunnerResource>> findAllTestCaseRunnersWithRunners(
            @PathVariable String testCaseId,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TestCaseInstanceRunner> testModelPage = this.testCaseService
                .findAllInstanceRunnersByTestCaseId(testCaseId, pageable);
        List<InstanceRunnerResource> instanceRunnerResources = testModelPage.get()
                .map(testCaseInstanceRunner -> conversionService
                        .convert(testCaseInstanceRunner, InstanceRunnerResource.class)).collect(
                        Collectors.toList());
        return ResponseEntity.ok(new PageImpl(instanceRunnerResources, pageable, testModelPage.getTotalElements()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCaseResource> findTestByTestId(@PathVariable String id) {
        return ResponseEntity.ok(conversionService.convert(this.testCaseService.findById(id), TestCaseResource.class));
    }

}
