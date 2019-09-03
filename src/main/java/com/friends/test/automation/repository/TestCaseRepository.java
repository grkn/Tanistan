package com.friends.test.automation.repository;

import com.friends.test.automation.entity.TestCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TestCaseRepository extends BaseTanistanJpaRepository<TestCase> {

    Page<TestCase> findAllByUserEntityIdAndTestProjectId(String userId, String projectId, Pageable pageable);

    Optional<Boolean> existsByName(String name);

    List<TestCase> findAllByUserEntityIdAndTestSuiteIdAndTestProjectIdOrderByCreatedDateDesc(String userId,
            String suiteId, String projectId);

    Optional<TestCase> findByIdAndTestProjectId(String id, String projectId);
}
