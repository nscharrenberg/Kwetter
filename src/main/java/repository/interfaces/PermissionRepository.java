package repository.interfaces;

import domain.Permission;
import exceptions.NameNotUniqueException;

import java.util.List;

public interface PermissionRepository {
    List<Permission> all();
    Permission getById(int id);
    Permission getByName(String name);
    Permission create(Permission permission) throws NameNotUniqueException, ClassNotFoundException;
    Permission update(Permission permission) throws NameNotUniqueException;
    boolean delete(Permission permission) throws ClassNotFoundException;
}
