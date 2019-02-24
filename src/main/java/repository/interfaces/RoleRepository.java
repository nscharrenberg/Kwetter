/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package repository.interfaces;

import exception.UsernameNotUniqueException;
import model.Permission;
import model.Role;

import java.util.List;
import java.util.Set;

public interface RoleRepository {
    List<Role> getRoles();
    Role getRoleById(int id);
    Role getRoleByName(String name);
    void create(Role role) throws UsernameNotUniqueException;
    void update(Role role) throws UsernameNotUniqueException;
    void addPermission(Role role, Permission permission);
    void addPermissions(Role role, Set<Permission> permissions);
    void removePermission(Role role, Permission permission);
}
