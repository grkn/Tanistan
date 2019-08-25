package com.friends.tanistan.entity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class TestModel extends TanistanBaseEntity<String>{

    @Lob
    private String testCommands;

    private String name;

    @ManyToOne
    private UserEntity userEntity;

    public String getTestCommands() {
        return testCommands;
    }

    public void setTestCommands(String testCommands) {
        this.testCommands = testCommands;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
