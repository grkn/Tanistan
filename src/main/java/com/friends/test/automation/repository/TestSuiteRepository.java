package com.friends.test.automation.repository;

import com.friends.test.automation.entity.TestSuite;

import java.util.Optional;

public interface TestSuiteRepository extends BaseTanistanJpaRepository<TestSuite> {

    Optional<TestSuite> findByIdAndTestProjectId(String id, String projectId);
    TestSuite findByParentIsNull();
}
