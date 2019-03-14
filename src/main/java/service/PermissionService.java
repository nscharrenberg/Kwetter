package service;

import domain.Permission;
import exceptions.CreationFailedException;
import exceptions.InvalidContentException;
import exceptions.NameNotUniqueException;
import exceptions.NotFoundException;
import repository.interfaces.JPA;
import repository.interfaces.PermissionRepository;
import responses.HttpStatusCodes;
import responses.ObjectResponse;
import utils.Logger;

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
    public List<Permission> all() {
        return pr.all();
    }

    /**
     * Get a permission by its id
     * @param id - the id of the permission
     * @return a permission
     * @throws InvalidContentException
     * @throws NotFoundException
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
     * @throws InvalidContentException
     * @throws NotFoundException
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

        if(pr.getByName(permission.getName()) != null) {
            return new ObjectResponse<>(HttpStatusCodes.CONFLICT, "Permission with name " + permission.getName() + " already exists.");
        }

        Permission created = pr.create(permission);
        if(created != null) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "Permission with name: " + permission.getName() + " created", permission);
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
        if(permission.getName().isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "name can not be empty");
        }

        if(permission.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        ObjectResponse<Permission> getByIdResponse = getById(permission.getId());

        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        ObjectResponse<Permission> getByNameResponse = getByName(permission.getName());

        if(getByNameResponse.getObject() != null && permission.getId() != getByNameResponse.getObject().getId()) {
            return getByNameResponse;
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
