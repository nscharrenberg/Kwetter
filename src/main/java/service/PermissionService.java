package service;

import com.sun.nio.sctp.IllegalUnbindException;
import domain.Permission;
import exceptions.InvalidContentException;
import repository.interfaces.JPA;
import repository.interfaces.PermissionRepository;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import java.util.List;

public class PermissionService {

    @Context
    private HttpServletRequest servletRequest;

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
    public Permission getById(int id) throws InvalidContentException, NotFoundException {
        if(id <= 0) {
            throw new InvalidContentException("Invalid ID");
        }

        Permission permission = pr.getById(id);

        if(permission == null) {
            throw new NotFoundException("Permission not found");
        }

        return permission;
    }

    /**
     * Get a permission by its name
     * @param name - the name of the permission
     * @return a permission
     * @throws IllegalArgumentException
     * @throws NotFoundException
     */
    public Permission getByName(String name) throws InvalidContentException, NotFoundException {
        if(name.isEmpty()) {
            throw new InvalidContentException("name can not be empty");
        }

        Permission permission = pr.getByName(name);

        if(permission == null) {
            throw new NotFoundException("Permission not found");
        }

        return permission;
    }

    /**
     * Create a new permission
     * @param permission - the permission information
     * @return the newly created permission
     * @throws InvalidContentException
     */
    public Permission create(Permission permission) throws InvalidContentException {
        if(permission.getName().isEmpty()) {
            throw new InvalidContentException("name can not be empty");
        }

        return pr.create(permission);
    }

    /**
     * Update an existing permission
     * @param permission - the new permission information with an existing permission id
     * @return the updated permission
     * @throws InvalidContentException
     */
    public Permission update(Permission permission) throws InvalidContentException {
        if(permission.getName().isEmpty()) {
            throw new InvalidContentException("name can not be empty");
        }

        if(permission.getId() <= 0) {
            throw new InvalidContentException("Invalid ID");
        }

        return pr.update(permission);
    }

    /**
     * Delete an existing permission
     * @param permission - the permission to be deleted
     * @return a boolean wether or not the permission is deleted.
     * @throws InvalidContentException
     */
    public boolean delete(Permission permission) throws InvalidContentException {
        if(permission.getId() <= 0) {
            throw new InvalidContentException("Invalid ID");
        }

        return pr.delete(permission);
    }
}
