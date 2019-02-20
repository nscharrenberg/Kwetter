/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package repository;

import model.Permission;
import model.Role;

import java.util.List;

public interface RoleRepository {
    public List<Role> getRoles();
    public Role getRoleById(int id);
    public Role getRoleByName(String name);
    public Role create(Role role);
    public Role update(Role role);
    public Role addPermission(Role role, Permission permission);
    public Role addPermissions(Role role, List<Permission> permissions);
    public Role removePermission(Role role, Permission permission);
}
