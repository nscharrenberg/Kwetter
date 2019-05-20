package com.nscharrenberg.kwetter.repository.collection;

import com.google.common.collect.Iterables;
import com.nscharrenberg.kwetter.domain.Permission;
import com.nscharrenberg.kwetter.repository.interfaces.PermissionRepository;

import javax.ejb.Stateful;
import javax.enterprise.inject.Default;
import java.util.ArrayList;
import java.util.List;

@Default
@Stateful
public class PermissionServiceCollImpl implements PermissionRepository {

    private List<Permission> permissions = new ArrayList<>();

    public PermissionServiceCollImpl() {
    }

    @Override
    public List<Permission> all() {
        return permissions;
    }

    @Override
    public Permission getById(int id) {
        return Iterables.tryFind(permissions, permission -> id == permission.getId()).orNull();
    }

    @Override
    public Permission getByName(String name) {
        return Iterables.tryFind(permissions, permission -> name.equals(permission.getName())).orNull();
    }

    @Override
    public Permission create(Permission permission) {
        permission.setId(all().size() + 1);
        if(permissions.add(permission)) {
            return permission;
        } else {
            return null;
        }
    }

    @Override
    public Permission update(Permission permission) {
        int index = Iterables.indexOf(permissions, p -> permission.getId() == p.getId());
        return permissions.set(index, permission);
    }

    @Override
    public boolean delete(Permission permission) {
        return permissions.remove(permission);
    }
}
