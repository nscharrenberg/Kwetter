package com.nscharrenberg.kwetter.dtos.permissions;

import com.nscharrenberg.kwetter.domain.Role;
import com.nscharrenberg.kwetter.dtos.roles.RoleCleanDto;

import java.util.Set;

public class PermissionDto {
    private Integer id;
    private String name;
    private Set<RoleCleanDto> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RoleCleanDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleCleanDto> roles) {
        this.roles = roles;
    }
}
