package com.nscharrenberg.kwetter.service;

import com.nscharrenberg.kwetter.domain.Permission;
import com.nscharrenberg.kwetter.repository.interfaces.JPA;
import com.nscharrenberg.kwetter.repository.interfaces.PermissionRepository;
import com.nscharrenberg.kwetter.responses.HttpStatusCodes;
import com.nscharrenberg.kwetter.responses.ObjectResponse;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class PermissionService {

    @Inject @JPA
    private PermissionRepository pr;

    /**
     * Get all permissions
     * @return a list of permissions
     */
    public ObjectResponse<List<Permission>> all() {
        List<Permission> permissions = pr.all();
        return new ObjectResponse<>(HttpStatusCodes.OK, permissions.size() + " permissions loaded", permissions);
    }

    /**
     * Get a permission by its id
     * @param id - the id of the permission
     * @return a permission
     */
    public ObjectResponse<Permission> getById(int id) {
        if(id <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        Permission permission = pr.getById(id);

        if(permission == null) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "Permission not found");
        }

        return new ObjectResponse<>(HttpStatusCodes.OK, "Permission with name: " + permission.getName() + " found", permission);
    }

    /**
     * Get a permission by its name
     * @param name - the name of the permission
     * @return a permission
     */
    public ObjectResponse<Permission> getByName(String name) {
        if(name.isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "name can not be empty");
        }

        Permission permission = pr.getByName(name);

        if(permission == null) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "Permission not found");
        }

        return new ObjectResponse<>(HttpStatusCodes.OK, "Permission with name: " + permission.getName() + " found", permission);
    }

    /**
     * Create a new permission
     * @param permission - the permission information
     * @return the newly created permission
     */
    public ObjectResponse<Permission> create(Permission permission) {
        if(permission.getName().isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "name can not be empty");
        }

        ObjectResponse<Permission> getByNameResponse = getByName(permission.getName());

        if(getByNameResponse.getObject() != null) {
            return new ObjectResponse<>(HttpStatusCodes.CONFLICT, "Permission with name " + permission.getName() + " already exists.");
        }

        Permission created = pr.create(permission);

        if(created != null) {
            return new ObjectResponse<>(HttpStatusCodes.CREATED, "Permission with name: " + permission.getName() + " created", permission);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not create a new permission due to an unknown error");
        }
    }

    /**
     * Update an existing permission
     * @param permission - the new permission information with an existing permission id
     * @return the updated permission
     */
    public ObjectResponse<Permission> update(Permission permission) {
        if(permission.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        if(permission.getName().isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "name can not be empty");
        }

        ObjectResponse<Permission> getByIdResponse = getById(permission.getId());

        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        ObjectResponse<Permission> getByNameResponse = getByName(permission.getName());

        if(getByNameResponse.getObject() != null && permission.getId() != getByNameResponse.getObject().getId()) {
            return new ObjectResponse<>(HttpStatusCodes.CONFLICT, "Permission with name " + permission.getName() + " already exists.");
        }

        Permission result =  pr.update(permission);
        if(result != null) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "Permission with name: " + permission.getName() + " has been updated", permission);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not update an existing permission due to an unknown error");
        }
    }

    /**
     * Delete an existing permission
     * @param permission - the permission to be deleted
     * @return a boolean wether or not the permission is deleted.
     */
    public ObjectResponse<Permission> delete(Permission permission) {
        if(permission.getId() <= 0) {
            return new ObjectResponse<Permission>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        ObjectResponse<Permission> getByIdResponse = getById(permission.getId());
        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        boolean deleted =  pr.delete(permission);

        if(deleted) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "Permission with name " + permission.getName() + " has been deleted");
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Permission with name " + permission.getName() + "could not be deleted due to an unknown error");
        }
    }
}
