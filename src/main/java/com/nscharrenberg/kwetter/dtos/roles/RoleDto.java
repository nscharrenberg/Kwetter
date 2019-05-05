package com.nscharrenberg.kwetter.dtos.roles;

import com.nscharrenberg.kwetter.dtos.permissions.PermissionCleanDto;

import java.util.Set;

public class RoleDto {
    private Integer id;
    private String name;
    private Set<PermissionCleanDto> permissions;

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

    public Set<PermissionCleanDto> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionCleanDto> permissions) {
        this.permissions = permissions;
    }
}
