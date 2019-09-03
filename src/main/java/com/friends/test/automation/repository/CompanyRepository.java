package com.friends.test.automation.repository;

import com.friends.test.automation.entity.Company;

public interface CompanyRepository extends BaseTanistanJpaRepository<Company> {

    Company findByNameIgnoreCaseContains(String name);
}
