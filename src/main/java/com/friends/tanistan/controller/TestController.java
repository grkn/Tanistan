package com.friends.tanistan.controller;

import com.friends.tanistan.controller.driver.TestCommandDto;
import com.friends.tanistan.controller.driver.TestCommandsResource;
import com.friends.tanistan.entity.TestModel;
import com.friends.tanistan.service.TestCommandService;
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

    private final TestCommandService testCommandService;
    private final ConversionService conversionService;

    public TestController(TestCommandService testCommandService,
            ConversionService conversionService) {
        this.testCommandService = testCommandService;
        this.conversionService = conversionService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<TestCommandsResource> createTestCommands(@PathVariable String userId, @RequestBody
            TestCommandDto testCommandDto) {
        TestModel testModel = this.testCommandService
                .save(userId, conversionService.convert(testCommandDto, TestModel.class));
        TestCommandsResource testCommandsResource = conversionService.convert(testModel, TestCommandsResource.class);
        return ResponseEntity.ok(testCommandsResource);
    }

    @GetMapping("/user/{userId}/all")
    public ResponseEntity<Page<TestCommandsResource>> findAllTestCommandsForUser(@PathVariable String userId,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TestModel> testModelPage = this.testCommandService.findAllByUserId(userId, pageable);
        List<TestCommandsResource> testCommandsResourceSet = testModelPage.get()
                .map(testModel -> conversionService.convert(testModel, TestCommandsResource.class)).collect(
                        Collectors.toList());
        return ResponseEntity.ok(new PageImpl(testCommandsResourceSet, pageable, testModelPage.getTotalElements()));
    }

}
