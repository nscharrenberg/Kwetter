package service;

import domain.Permission;
import domain.Role;
import exceptions.ActionForbiddenException;
import exceptions.NameNotUniqueException;
import repository.interfaces.PermissionRepository;
import repository.interfaces.RoleRepository;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;

public class RoleService {

    @Inject @Default
    private RoleRepository rr;

    @Inject @Default
    private PermissionRepository pr;

    List<Role> all() {
        return rr.all();
    }

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

    Role create(Role role) throws NameNotUniqueException, ClassNotFoundException {
        if(role.getName().isEmpty()) {
            throw new IllegalArgumentException("name can not be empty");
        }

        return rr.create(role);
    }

    Role update(Role role) throws NameNotUniqueException {
        if(role.getName().isEmpty()) {
            throw new IllegalArgumentException("name can not be empty");
        }

        if(role.getId() <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        return rr.update(role);
    }

    boolean delete(Role role) throws ClassNotFoundException {
        if(role.getId() <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        return rr.delete(role);
    }

    Role addPermission(Role role, Permission permission) throws ClassNotFoundException, NameNotUniqueException, ActionForbiddenException {
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

        return rr.addPermission(role, permission);
    }

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
