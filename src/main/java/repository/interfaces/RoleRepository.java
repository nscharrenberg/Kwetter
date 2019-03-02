package repository.interfaces;

import domain.Permission;
import domain.Role;
import exceptions.NameNotUniqueException;

import java.util.List;

public interface RoleRepository {
    List<Role> all();
    Role getById(int id);
    Role getByName(String name);
    Role create(Role role);
    Role update(Role role);
    boolean delete(Role role);
    Role addPermission(Role role, Permission permission);
    Role removePermission(Role role, Permission permission);
}
