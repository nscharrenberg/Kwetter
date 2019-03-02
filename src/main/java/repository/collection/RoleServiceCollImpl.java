package repository.collection;

import com.google.common.collect.Iterables;
import domain.Permission;
import domain.Role;
import exceptions.NameNotUniqueException;
import repository.interfaces.RoleRepository;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import java.util.ArrayList;
import java.util.List;

@Default
@Stateful
public class RoleServiceCollImpl implements RoleRepository {

    List<Role> roles = new ArrayList<>();

    public RoleServiceCollImpl() {
    }

    @Override
    public List<Role> all() {
        return roles;
    }

    @Override
    public Role getById(int id) {
        return Iterables.tryFind(roles, role -> id == role.getId()).orNull();
    }

    @Override
    public Role getByName(String name) {
        return Iterables.tryFind(roles, role -> name.equals(role.getName())).orNull();
    }

    @Override
    public Role create(Role role) {
        role.setId(all().size() + 1);
        if(roles.add(role)) {
            return role;
        } else {
            return null;
        }
    }

    @Override
    public Role update(Role role) {
        int index = Iterables.indexOf(roles, r -> role.getId() == r.getId());
        return roles.set(index, role);
    }

    @Override
    public boolean delete(Role role) {
        return roles.remove(role);
    }

    @Override
    public Role addPermission(Role role, Permission permission) {
        role.addPermission(permission);
        return update(role);
    }

    @Override
    public Role removePermission(Role role, Permission permission) {
        role.removePermission(permission);
        return update(role);
    }
}
