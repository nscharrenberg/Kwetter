package repository.collection;

import com.google.common.collect.Iterables;
import domain.Permission;
import exceptions.NameNotUniqueException;
import repository.interfaces.PermissionRepository;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import java.util.ArrayList;
import java.util.List;

@Default
@Stateless
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
    public Permission create(Permission permission) throws NameNotUniqueException, ClassNotFoundException {
        Permission result = Iterables.tryFind(permissions, p -> permission.getName().equals(p.getName())).orNull();

        if(result != null) {
            throw new NameNotUniqueException("Permission name already exists.");
        }

        if(permissions.add(permission)) {
            return permission;
        } else {
            throw new ClassNotFoundException("Failed to create permission");
        }
    }

    @Override
    public Permission update(Permission permission) throws NameNotUniqueException {
        Permission result = Iterables.tryFind(permissions, p -> permission.getName().equals(p.getName()) && permission.getId() == p.getId()).orNull();

        if(result != null) {
            throw new NameNotUniqueException("Permission name already exists.");
        }

        int index = Iterables.indexOf(permissions, p -> permission.getId() == p.getId());

        return permissions.set(index, permission);
    }

    @Override
    public boolean delete(Permission permission) throws ClassNotFoundException {
        Permission result = Iterables.tryFind(permissions, p -> permission.getId() == p.getId()).orNull();

        if(result == null) {
            throw new ClassNotFoundException("Permission with id: " + permission.getId() + " and name: " + permission.getName() + " could not be found");
        }

        return permissions.remove(permission);
    }
}
