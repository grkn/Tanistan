package com.friends.test.automation.repository;

import com.friends.test.automation.entity.TestCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TestCaseRepository extends BaseTanistanJpaRepository<TestCase> {

    Page<TestCase> findAllByUserEntityId(String userId, Pageable pageable);

    Optional<Boolean> existsByName(String name);

    List<TestCase> findAllByUserEntityIdAndTestSuiteIdOrderByCreatedDateDesc(String userId, String suiteId);
}
