package repository.interfaces;

import domain.Permission;
import exceptions.NameNotUniqueException;

import java.util.List;

public interface PermissionRepository {
    List<Permission> all();
    Permission getById(int id);
    Permission getByName(String name);
    Permission create(Permission permission);
    Permission update(Permission permission);
    boolean delete(Permission permission);
}
