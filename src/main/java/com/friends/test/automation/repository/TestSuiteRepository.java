package com.friends.test.automation.repository;

import com.friends.test.automation.entity.TestSuite;

public interface TestSuiteRepository extends BaseTanistanJpaRepository<TestSuite> {

    TestSuite findByParentIsNull();
}
