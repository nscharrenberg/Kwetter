package service;

import domain.Permission;
import domain.Role;
import exceptions.ActionForbiddenException;
import exceptions.NameNotUniqueException;
import repository.interfaces.PermissionRepository;
import repository.interfaces.RoleRepository;
import repository.interfaces.SelectedContext;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;

public class RoleService {

    @Inject @SelectedContext
    private RoleRepository rr;

    @Inject
    private PermissionService pr;

    /**
     * Get all roles
     * @return a list of roles
     */
    List<Role> all() {
        return rr.all();
    }

    /**
     * Get a role by its id
     * @param id - the id of the role
     * @return a role
     */
    Role getById(int id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        Role role = rr.getById(id);

        if(role == null) {
            throw new NotFoundException("Role not found");
        }

        return role;
    }

    /**
     * Get a role by its name
     * @param name - the name of the role
     * @return a role
     */
    Role getByName(String name) {
        if(name.isEmpty()) {
            throw new IllegalArgumentException("name can not be empty");
        }

        Role role = rr.getByName(name);

        if(role == null) {
            throw new NotFoundException("Role not found");
        }

        return role;
    }

    /**
     * Create a new role
     * @param role - the role information
     * @return the newly created role
     * @throws NameNotUniqueException
     * @throws ClassNotFoundException
     */
    Role create(Role role) throws NameNotUniqueException, ClassNotFoundException {
        if(role.getName().isEmpty()) {
            throw new IllegalArgumentException("name can not be empty");
        }

        return rr.create(role);
    }

    /**
     * Update an existing role
     * @param role - the new role information with an existing role id
     * @return the updated role
     * @throws NameNotUniqueException
     */
    Role update(Role role) throws NameNotUniqueException {
        if(role.getName().isEmpty()) {
            throw new IllegalArgumentException("name can not be empty");
        }

        if(role.getId() <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        return rr.update(role);
    }

    /**
     * Delete an existing role
     * @param role - the role to be deleted
     * @return a boolean wether or not the role is deleted.
     * @throws ClassNotFoundException
     */
    boolean delete(Role role) throws ClassNotFoundException {
        if(role.getId() <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        return rr.delete(role);
    }


    /**
     * Add a permission to an existing role
     * @param role - the role
     * @param permission - the permission
     * @return the role
     * @throws ClassNotFoundException
     * @throws NameNotUniqueException
     * @throws ActionForbiddenException
     */
    Role addPermission(Role role, Permission permission, boolean canCreate, boolean canRead, boolean canUpdate, boolean canDelete) throws ClassNotFoundException, NameNotUniqueException, ActionForbiddenException {
        if(role.getId() <= 0) {
            throw new IllegalArgumentException("Invalid role ID");
        }

        if(permission.getId() <= 0) {
            throw new IllegalArgumentException("Invalid permission ID");
        }

        Permission p = pr.getById(permission.getId());

        if(p == null) {
            throw new NotFoundException("Permission not found");
        }

        Role r = rr.getById(role.getId());

        if(r == null) {
            throw new NotFoundException("Role not found");
        }

        if(r.getPermissions().contains(permission)) {
            throw new ActionForbiddenException("Role: " + role.getName() + " already has the permission " + permission.getName());
        }

        return rr.addPermission(role, permission, canCreate, canRead, canUpdate, canDelete);
    }

    /**
     * Remove a permission from an existing role
     * @param role - the role
     * @param permission - the permission
     * @return the role
     * @throws ClassNotFoundException
     * @throws NameNotUniqueException
     * @throws ActionForbiddenException
     */
    Role removePermission(Role role, Permission permission) throws ClassNotFoundException, NameNotUniqueException, ActionForbiddenException {
        if(role.getId() <= 0) {
            throw new IllegalArgumentException("Invalid role ID");
        }

        if(permission.getId() <= 0) {
            throw new IllegalArgumentException("Invalid permission ID");
        }

        Permission p = pr.getById(permission.getId());

        if(p == null) {
            throw new NotFoundException("Permission not found");
        }

        Role r = rr.getById(role.getId());

        if(r == null) {
            throw new NotFoundException("Role not found");
        }

        if(!r.getPermissions().contains(permission)) {
            throw new ActionForbiddenException("Role: " + role.getName() + " does not have the permission " + permission.getName());
        }

        return rr.removePermission(role, permission);
    }
}
