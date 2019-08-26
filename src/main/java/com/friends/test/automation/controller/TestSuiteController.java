package com.friends.test.automation.controller;

import com.friends.test.automation.controller.driver.TestCaseResource;
import com.friends.test.automation.controller.driver.TestSuiteDto;
import com.friends.test.automation.controller.resource.TestSuiteResource;
import com.friends.test.automation.entity.TestSuite;
import com.friends.test.automation.service.TestCaseService;
import com.friends.test.automation.service.TestSuiteService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/tanistan/testsuite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TestSuiteController {

    private final ConversionService conversionService;
    private final TestSuiteService testSuiteService;
    private final TestCaseService testCaseService;

    public TestSuiteController(ConversionService conversionService,
            TestSuiteService testSuiteService, TestCaseService testCaseService) {
        this.conversionService = conversionService;
        this.testSuiteService = testSuiteService;
        this.testCaseService = testCaseService;
    }

    @PostMapping
    public ResponseEntity<TestSuiteResource> createTestSuite(@RequestBody TestSuiteDto testSuiteDto) {
        TestSuite testSuite = conversionService.convert(testSuiteDto, TestSuite.class);
        TestSuiteResource resource = conversionService
                .convert(testSuiteService.createTestSuite(testSuite), TestSuiteResource.class);
        return ResponseEntity.ok(resource);
    }

    @PostMapping("/{id}")
    public ResponseEntity<TestSuiteResource> updateTestSuite(@PathVariable String id,
            @RequestBody TestSuiteDto testSuiteDto) {
        TestSuite testSuite = conversionService.convert(testSuiteDto, TestSuite.class);
        testSuite.setId(id);
        TestSuiteResource resource = conversionService
                .convert(testSuiteService.createTestSuite(testSuite), TestSuiteResource.class);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/children/{parentId}")
    public ResponseEntity<TestSuiteResource> updateTestSuite(@PathVariable String parentId) {
        TestSuiteResource resource = conversionService
                .convert(testSuiteService.getChildren(parentId), TestSuiteResource.class);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestSuiteResource> getTestSuiteById(@PathVariable String id) {
        TestSuiteResource resource = conversionService
                .convert(testSuiteService.getTestSuiteById(id), TestSuiteResource.class);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/root")
    public ResponseEntity<TestSuiteResource> getTestSuiteById() {
        TestSuiteResource resource = conversionService
                .convert(testSuiteService.getRoot(), TestSuiteResource.class);
        return ResponseEntity.ok(resource);
    }

    @PostMapping("/{suiteId}/addtest")
    public ResponseEntity<TestSuiteResource> ddTestCaseToTestSuite(@PathVariable String suiteId,
            @RequestBody List<String> testIds) {
        TestSuiteResource resource = conversionService
                .convert(testSuiteService.addTestCaseToTestSuite(suiteId, testIds), TestSuiteResource.class);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/{suiteId}/testcases")
    public ResponseEntity<Set<TestCaseResource>> getTestCaseBySuiteId(@PathVariable String suiteId) {
        Set<TestCaseResource> testCases = testSuiteService.getTestCasesBySuiteId(suiteId).stream()
                .map(testCase -> conversionService.convert(testCase, TestCaseResource.class))
                .collect(Collectors.toSet());
        return ResponseEntity.ok(testCases);
    }

    @DeleteMapping("/{suiteId}/testcase/{testCaseId}")
    public ResponseEntity<Void> deleteTestCaseFromSuite(@PathVariable String suiteId, String testCaseId) {
        testSuiteService.deleteTestCase(suiteId,testCaseId);
        return ResponseEntity.noContent().build();
    }
}
