/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package service;

import model.Permission;
import model.Role;

import java.util.List;

public class RoleService {

    /**
     * Get all Roles
     * @return
     */
    public List<Role> getRoles() {
        //todo: implement getRoles method
        return null;
    }

    /**
     * Get role by id
     * @param id
     * @return
     */
    public Role getRoleById(int id) {
        //todo: implement getRoleById method
        return null;
    }

    /**
     * Get role by name
     * @param name
     * @return
     */
    public Role getRoleByName(String name) {
        //todo: impleemnt getRoleByName method
        return null;
    }

    /**
     * Create a new Role
     * @param role
     * @return
     */
    public Role create(Role role) {
        //todo: implement create method
        return null;
    }

    /**
     * Update an existing Role
     * @param role
     * @return
     */
    public Role update(Role role) {
        //todo: implement update method
        return null;
    }

    /**
     * Add a permission to a specific role
     * @param role
     * @param permission
     * @return
     */
    public Role addPermission(Role role, Permission permission) {
        //todo: implement addPermission method
        return null;
    }

    /**
     * Add multiple permissions to a specific role
     * @param role
     * @param permissions
     * @return
     */
    public Role addPermissions(Role role, List<Permission> permissions) {
        //todo: implement addPermissions method
        return null;
    }

    /**
     * Remove a permission from a specific role
     * @param role
     * @param permission
     * @return
     */
    public Role removePermission(Role role, Permission permission) {
        //todo: implement removePermission method
        return null;
    }
}
