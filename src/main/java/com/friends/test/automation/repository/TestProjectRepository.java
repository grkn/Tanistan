package com.friends.test.automation.repository;

import com.friends.test.automation.entity.TestProject;

import java.util.List;
import java.util.Optional;

public interface TestProjectRepository extends BaseTanistanJpaRepository<TestProject> {

    List<TestProject> findAllByUserEntitiesId(String userId);
    Optional<TestProject> findByIdAndUserEntitiesId(String id,String userId);
}
