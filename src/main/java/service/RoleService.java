package service;

import domain.Permission;
import domain.Role;
import exceptions.*;
import repository.interfaces.JPA;
import repository.interfaces.RoleRepository;
import javax.inject.Inject;
import java.util.List;

public class RoleService {

    @Inject @JPA
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
     * @throws NotFoundException
     * @throws InvalidContentException
     */
    Role getById(int id) throws NotFoundException, InvalidContentException {
        if(id <= 0) {
            throw new InvalidContentException("Invalid ID");
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
     * @throws InvalidContentException
     * @throws NotFoundException
     */
    Role getByName(String name) throws InvalidContentException, NotFoundException {
        if(name.isEmpty()) {
            throw new InvalidContentException("name can not be empty");
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
     * @throws InvalidContentException
     * @throws NameNotUniqueException
     * @throws CreationFailedException
     */
    Role create(Role role) throws InvalidContentException, NameNotUniqueException, CreationFailedException {
        if(role.getName().isEmpty()) {
            throw new InvalidContentException("name can not be empty");
        }

        if(rr.getByName(role.getName()) != null) {
            throw new NameNotUniqueException("Role with name " + role.getName() + " already exists.");
        }

        Role created = rr.create(role);

        if(created != null) {
            return created;
        } else {
            throw new CreationFailedException("Could not create a new role due to an unknown error");
        }
    }

    /**
     * Update an existing role
     * @param role - the new role information with an existing role id
     * @return the updated role
     * @throws InvalidContentException
     */
    Role update(Role role) throws InvalidContentException, NameNotUniqueException {
        if(role.getName().isEmpty()) {
            throw new InvalidContentException("name can not be empty");
        }

        if(role.getId() <= 0) {
            throw new InvalidContentException("Invalid ID");
        }

        Role uniqueRole = rr.getByName(role.getName());
        if(uniqueRole != null && role.getId() != uniqueRole.getId()) {
            throw new NameNotUniqueException("Role with name " + role.getName() + " already exists.");
        }

        return rr.update(role);
    }

    /**
     * Delete an existing role
     * @param role - the role to be deleted
     * @return a boolean wether or not the role is deleted.
     * @throws InvalidContentException
     * @throws NotFoundException
     */
    boolean delete(Role role) throws InvalidContentException, NotFoundException {
        if(role.getId() <= 0) {
            throw new InvalidContentException("Invalid ID");
        }

        if(rr.getById(role.getId()) == null) {
            throw new NotFoundException("Role not found");
        }

        return rr.delete(role);
    }


    /**
     * Add a permission to an existing role
     * @param role - the role
     * @param permission - the permission
     * @return the role
     * @throws InvalidContentException
     * @throws ActionForbiddenException
     * @throws NotFoundException
     */
    Role addPermission(Role role, Permission permission, boolean canCreate, boolean canRead, boolean canUpdate, boolean canDelete) throws ActionForbiddenException, InvalidContentException, NotFoundException {
        if(role.getId() <= 0) {
            throw new InvalidContentException("Invalid role ID");
        }

        if(permission.getId() <= 0) {
            throw new InvalidContentException("Invalid permission ID");
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
     * @throws InvalidContentException
     * @throws ActionForbiddenException
     * @throws NotFoundException
     */
    Role removePermission(Role role, Permission permission) throws ActionForbiddenException, InvalidContentException, NotFoundException {
        if(role.getId() <= 0) {
            throw new InvalidContentException("Invalid role ID");
        }

        if(permission.getId() <= 0) {
            throw new InvalidContentException("Invalid permission ID");
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
