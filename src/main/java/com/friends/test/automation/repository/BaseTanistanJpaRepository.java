package com.friends.test.automation.repository;

import com.friends.test.automation.entity.TanistanBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BaseTanistanJpaRepository<T extends TanistanBaseEntity<String>> extends CrudRepository<T, String>, JpaRepository<T,String> {

}
