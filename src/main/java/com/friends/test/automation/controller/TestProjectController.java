package com.friends.test.automation.controller;

import com.friends.test.automation.controller.dto.AddAndRemoveDto;
import com.friends.test.automation.controller.dto.TestProjectDto;
import com.friends.test.automation.controller.resource.TestProjectResource;
import com.friends.test.automation.entity.TestProject;
import com.friends.test.automation.service.TestProjectService;
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

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/tanistan/testproject", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TestProjectController {

    private final TestProjectService testProjectService;
    private final ConversionService conversionService;

    public TestProjectController(TestProjectService testProjectService,
            ConversionService conversionService) {
        this.testProjectService = testProjectService;
        this.conversionService = conversionService;
    }

    @PostMapping
    public ResponseEntity<TestProjectResource> create(@RequestBody @Valid TestProjectDto testProjectDto) {
        TestProject convertedProject = conversionService.convert(testProjectDto, TestProject.class);
        TestProject testProject = testProjectService.createOrUpdate(convertedProject);
        return ResponseEntity.ok(conversionService.convert(testProject, TestProjectResource.class));
    }

    @GetMapping("/user/{userId}/all")
    public ResponseEntity<List<TestProjectResource>> create(@PathVariable String userId) {
        List<TestProject> testProject = testProjectService.findAll(userId);
        List<TestProjectResource> testProjectResources = testProject.stream()
                .map(project -> conversionService.convert(project, TestProjectResource.class)).collect(
                        Collectors.toList());
        return ResponseEntity.ok(testProjectResources);
    }

    @GetMapping("/{id}/user/{userId}")
    public ResponseEntity<TestProjectResource> findById(@PathVariable String id, @PathVariable String userId) {
        TestProject testProject = testProjectService.findById(id, userId);
        return ResponseEntity.ok(conversionService.convert(testProject, TestProjectResource.class));
    }


    @PostMapping("/{id}/user/{userId}/addandremove")
    public ResponseEntity<TestProjectResource> userOperations(@PathVariable String id, @PathVariable String userId,
            @RequestBody AddAndRemoveDto addAndRemoveDto) {
        TestProject testProject = testProjectService
                .addAndRemove(userId, id, addAndRemoveDto.getAdd(), addAndRemoveDto.getRemove());
        return ResponseEntity.ok(conversionService.convert(testProject, TestProjectResource.class));
    }

    @PostMapping("/{id}")
    public ResponseEntity<TestProjectResource> update(@PathVariable String id,
            @RequestBody @Valid TestProjectDto testProjectDto) {
        TestProject convertedProject = conversionService.convert(testProjectDto, TestProject.class);
        convertedProject.setId(id);
        TestProject testProject = testProjectService.createOrUpdate(convertedProject);
        return ResponseEntity.ok(conversionService.convert(testProject, TestProjectResource.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        testProjectService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
