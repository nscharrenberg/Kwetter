package repository.interfaces;

import domain.Permission;
import domain.Role;
import exceptions.NameNotUniqueException;

import java.util.List;

public interface RoleRepository {
    List<Role> all();
    Role getById(int id);
    Role getByName(String name);
    Role create(Role role) throws NameNotUniqueException, ClassNotFoundException;
    Role update(Role role) throws NameNotUniqueException;
    boolean delete(Role role) throws ClassNotFoundException;
    Role addPermission(Role role, Permission permission, boolean canCreate, boolean canRead, boolean canUpdate, boolean canDelete) throws ClassNotFoundException, NameNotUniqueException;
    Role removePermission(Role role, Permission permission) throws ClassNotFoundException, NameNotUniqueException;
}
