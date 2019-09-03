package com.friends.test.automation.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Company extends TanistanBaseEntity<String> {

    private String name;

    @OneToMany(mappedBy = "company",fetch = FetchType.EAGER)
    private List<UserEntity> users;

    public Company() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }
}
