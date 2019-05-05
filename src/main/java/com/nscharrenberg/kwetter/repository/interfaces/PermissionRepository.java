package com.nscharrenberg.kwetter.repository.interfaces;

import com.nscharrenberg.kwetter.domain.Permission;
import com.nscharrenberg.kwetter.exceptions.NameNotUniqueException;

import java.util.List;

public interface PermissionRepository {
    List<Permission> all();
    Permission getById(int id);
    Permission getByName(String name);
    Permission create(Permission permission);
    Permission update(Permission permission);
    boolean delete(Permission permission);
}
