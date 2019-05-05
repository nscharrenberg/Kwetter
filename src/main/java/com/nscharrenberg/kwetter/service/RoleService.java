package com.nscharrenberg.kwetter.service;

import com.nscharrenberg.kwetter.domain.Permission;
import com.nscharrenberg.kwetter.domain.Role;
import com.nscharrenberg.kwetter.exceptions.*;
import com.nscharrenberg.kwetter.repository.interfaces.JPA;
import com.nscharrenberg.kwetter.repository.interfaces.RoleRepository;
import com.nscharrenberg.kwetter.responses.HttpStatusCodes;
import com.nscharrenberg.kwetter.responses.ObjectResponse;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class RoleService {

    @Inject @JPA
    private RoleRepository rr;

    @Inject
    private PermissionService pr;

    /**
     * Get all roles
     * @return a list of roles
     */
    public ObjectResponse<List<Role>> all() {
        List<Role> roles = rr.all();
        return new ObjectResponse<>(HttpStatusCodes.OK, roles.size() + " permissions loaded", roles);
    }

    /**
     * Get a role by its id
     * @param id - the id of the role
     * @return a role
     */
    public ObjectResponse<Role> getById(int id) {
        if(id <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        Role role = rr.getById(id);

        if(role == null) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "Role not found");
        }

        return new ObjectResponse<>(HttpStatusCodes.OK, "Role with name: " + role.getName() + " found", role);
    }

    /**
     * Get a role by its name
     * @param name - the name of the role
     * @return a role
     */
    public ObjectResponse<Role> getByName(String name) {
        if(name.isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "name can not be empty");
        }

        Role role = rr.getByName(name);

        if(role == null) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "Role not found");
        }

        return new ObjectResponse<>(HttpStatusCodes.OK, "Role with name: " + role.getName() + " found", role);
    }

    /**
     * Create a new role
     * @param role - the role information
     * @return the newly created role
     */
    public ObjectResponse<Role> create(Role role) {
        if(role.getName().isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "name can not be empty");
        }

        ObjectResponse<Role> getByNameResponse = getByName(role.getName());
        if(getByNameResponse.getObject() != null) {
            return new ObjectResponse<>(HttpStatusCodes.CONFLICT, "Role with name " + role.getName() + " already exists.");
        }

        Role created = rr.create(role);

        if(created != null) {
            return new ObjectResponse<>(HttpStatusCodes.CREATED, "Role with name: " + role.getName() + " created", role);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not create a new role due to an unknown error");
        }
    }

    /**
     * Update an existing role
     * @param role - the new role information with an existing role id
     * @return the updated role
     */
    public ObjectResponse<Role> update(Role role) {
        if(role.getName().isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "name can not be empty");
        }

        if(role.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        ObjectResponse<Role> getByIdResponse = getById(role.getId());

        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        ObjectResponse<Role> getByNameResponse = getByName(role.getName());
        if(getByNameResponse.getObject() != null && role.getId() != getByNameResponse.getObject().getId()) {
            return new ObjectResponse<>(HttpStatusCodes.CONFLICT, "Role with name " + role.getName() + " already exists.");
        }

        Role result = rr.update(role);
        if(result != null) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "Role with name: " + result.getName() + " has been updated", result);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not update an existing role due to an unknown error");
        }
    }

    /**
     * Delete an existing role
     * @param role - the role to be deleted
     * @return a boolean wether or not the role is deleted.
     */
    public ObjectResponse<Role> delete(Role role) {
        if(role.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        ObjectResponse<Role> getByIdResponse = getById(role.getId());
        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        boolean deleted = rr.delete(role);

        if(deleted) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "Role with name " + role.getName() + " has been deleted");
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Role with name " + role.getName() + "could not be deleted due to an unknown error");
        }
    }


    /**
     * Add a permission to an existing role
     * @param role - the role
     * @param permission - the permission
     * @return the role
     */
    public ObjectResponse<Role> addPermission(Role role, Permission permission) {
        if(role.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid Role ID");
        }

        if(permission.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid Permission ID");
        }

        ObjectResponse<Permission> getPermissionByIdResponse = pr.getById(permission.getId());

        if(getPermissionByIdResponse.getObject() == null) {
            return new ObjectResponse<>(getPermissionByIdResponse.getCode(), getPermissionByIdResponse.getMessage());
        }

        ObjectResponse<Role> getByIdResponse = getById(role.getId());

        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        if(getByIdResponse.getObject().getPermissions().contains(permission)) {
            return new ObjectResponse<>(HttpStatusCodes.FORBIDDEN, "Role: " + role.getName() + " already has the permission " + permission.getName());
        }

        Role result = rr.addPermission(role, permission);
        if(result != null) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "Permission with name: " + permission.getName() + " has been added to Role with name: " + role.getName(), result);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not add Permission to an existing role due to an unknown error");
        }
    }

    /**
     * Remove a permission from an existing role
     * @param role - the role
     * @param permission - the permission
     * @return the role
     */
    public ObjectResponse<Role> removePermission(Role role, Permission permission) {
        if(role.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid Role ID");
        }

        if(permission.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid Permissions ID");
        }

        ObjectResponse<Permission> getPermissionByIdResponse = pr.getById(permission.getId());

        if(getPermissionByIdResponse.getObject() == null) {
            return new ObjectResponse<>(getPermissionByIdResponse.getCode(), getPermissionByIdResponse.getMessage());
        }

        ObjectResponse<Role> getByIdResponse = getById(role.getId());

        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        if(!getByIdResponse.getObject().getPermissions().contains(permission)) {
            return new ObjectResponse<>(HttpStatusCodes.FORBIDDEN, "Role: " + role.getName() + " does not have the permission " + permission.getName());
        }

        Role result = rr.removePermission(role, permission);
        if(result != null) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "Permission with name: " + permission.getName() + " has been remove from Role with name: " + role.getName(), result);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not remove Permission from an existing role due to an unknown error");
        }
    }
}
