package com.nscharrenberg.kwetter.repository.interfaces;

import com.nscharrenberg.kwetter.domain.Permission;
import com.nscharrenberg.kwetter.domain.Role;

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
