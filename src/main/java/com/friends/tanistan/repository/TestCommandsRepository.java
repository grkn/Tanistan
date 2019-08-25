package com.friends.tanistan.repository;

import com.friends.tanistan.entity.TestModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TestCommandsRepository extends BaseTanistanJpaRepository<TestModel> {

    Page<TestModel> findAllByUserEntityId(String userId,Pageable pageable);
}
