package com.friends.test.automation.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"authority"})})
public class UserAuthorization extends TanistanBaseEntity<String> {

    private String authority;

    @ManyToMany
    @JoinTable(name = "user_auth_relation",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_entity_id"))
    private List<UserEntity> userEntity;

    public UserAuthorization() {

    }

    public UserAuthorization(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public List<UserEntity> getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(List<UserEntity> userEntity) {
        this.userEntity = userEntity;
    }
}
