package service;

import com.sun.nio.sctp.IllegalUnbindException;
import domain.Permission;
import exceptions.NameNotUniqueException;
import repository.interfaces.JPA;
import repository.interfaces.PermissionRepository;
import repository.interfaces.SelectedContext;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;

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
     */
    public Permission getById(int id) throws IllegalUnbindException, NotFoundException {
        if(id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
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
     */
    public Permission getByName(String name) throws IllegalArgumentException, NotFoundException {
        if(name.isEmpty()) {
            throw new IllegalArgumentException("name can not be empty");
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
     * @throws NameNotUniqueException
     * @throws ClassNotFoundException
     */
    public Permission create(Permission permission) throws NameNotUniqueException, ClassNotFoundException, IllegalArgumentException {
        if(permission.getName().isEmpty()) {
            throw new IllegalArgumentException("name can not be empty");
        }

        return pr.create(permission);
    }

    /**
     * Update an existing permission
     * @param permission - the new permission information with an existing permission id
     * @return the updated permission
     * @throws NameNotUniqueException
     */
    public Permission update(Permission permission) throws NameNotUniqueException, IllegalArgumentException {
        if(permission.getName().isEmpty()) {
            throw new IllegalArgumentException("name can not be empty");
        }

        if(permission.getId() <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        return pr.update(permission);
    }

    /**
     * Delete an existing permission
     * @param permission - the permission to be deleted
     * @return a boolean wether or not the permission is deleted.
     * @throws ClassNotFoundException
     */
    public boolean delete(Permission permission) throws ClassNotFoundException, IllegalArgumentException {
        if(permission.getId() <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        return pr.delete(permission);
    }
}
