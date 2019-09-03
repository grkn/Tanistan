package com.friends.test.automation.repository;

import com.friends.test.automation.entity.TestCaseInstanceRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TestCaseInstanceRunnerRepository extends BaseTanistanJpaRepository<TestCaseInstanceRunner> {

    Page<TestCaseInstanceRunner> findAllByTestCaseIdAndTestCaseTestProjectId(String testCaseId, String projectId,
            Pageable pageable);

}
