/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package service;

import exception.UsernameNotUniqueException;
import model.Permission;
import model.Role;
import repository.interfaces.JPA;
import repository.interfaces.RoleRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@Stateless
public class RoleService {

    @Inject
    @JPA
    private RoleRepository rr;

    public RoleService() {
    }

    /**
     * Get all Roles
     * @return
     */
    public List<Role> getRoles() {
        return rr.getRoles();
    }

    /**
     * Get role by id
     * @param id
     * @return
     */
    public Role getRoleById(int id) {
        return rr.getRoleById(id);
    }

    /**
     * Get role by name
     * @param name
     * @return
     */
    public Role getRoleByName(String name) {
        return rr.getRoleByName(name);
    }

    /**
     * Create a new Role
     * @param role
     * @return
     */
    public void create(Role role) throws UsernameNotUniqueException {
        rr.create(role);
    }

    /**
     * Update an existing Role
     * @param role
     * @return
     */
    public void update(Role role) throws UsernameNotUniqueException {
        rr.update(role);
    }

    /**
     * Add a permission to a specific role
     * @param role
     * @param permission
     * @return
     */
    public void addPermission(Role role, Permission permission) {
        rr.addPermission(role, permission);
    }

    /**
     * Add multiple permissions to a specific role
     * @param role
     * @param permissions
     * @return
     */
    public void addPermissions(Role role, Set<Permission> permissions) {
        rr.addPermissions(role, permissions);
    }

    /**
     * Remove a permission from a specific role
     * @param role
     * @param permission
     * @return
     */
    public void removePermission(Role role, Permission permission) {
         rr.removePermission(role, permission);
    }
}
