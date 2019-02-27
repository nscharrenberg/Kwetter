package service;

import domain.Permission;
import exceptions.NameNotUniqueException;
import repository.interfaces.PermissionRepository;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;

public class PermissionService {

    @Inject @Default
    private PermissionRepository pr;

    public List<Permission> all() {
        return pr.all();
    }

    public Permission getById(int id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        Permission permission = pr.getById(id);

        if(permission == null) {
            throw new NotFoundException("Permission not found");
        }

        return permission;
    }

    public Permission getByName(String name) {
        if(name.isEmpty()) {
            throw new IllegalArgumentException("name can not be empty");
        }

        Permission permission = pr.getByName(name);

        if(permission == null) {
            throw new NotFoundException("Permission not found");
        }

        return permission;
    }

    public Permission create(Permission permission) throws NameNotUniqueException, ClassNotFoundException {
        if(permission.getName().isEmpty()) {
            throw new IllegalArgumentException("name can not be empty");
        }

        return pr.create(permission);
    }

    public Permission update(Permission permission) throws NameNotUniqueException {
        if(permission.getName().isEmpty()) {
            throw new IllegalArgumentException("name can not be empty");
        }

        if(permission.getId() <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        return pr.update(permission);
    }

    public boolean delete(Permission permission) throws ClassNotFoundException {
        if(permission.getId() <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        return pr.delete(permission);
    }
}
