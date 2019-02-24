package repository.memory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import exception.UsernameNotUniqueException;
import model.Permission;
import model.Role;
import repository.interfaces.RoleRepository;

import javax.annotation.Nullable;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Stateless
@Default
public class RoleServiceImpl implements RoleRepository {

    private List<Role> roles = new ArrayList<>();

    @Override
    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public Role getRoleById(int id) {
        return Iterables.tryFind(roles, r -> id == r.getId()).orNull();
    }

    @Override
    public Role getRoleByName(String name) {
        return Iterables.tryFind(roles, r -> name.equals(r.getName())).orNull();
    }

    @Override
    public void create(Role role) throws UsernameNotUniqueException {
        Role result = Iterables.tryFind(roles, r -> role.getName().equals(r.getName())).orNull();

        if(result != null) {
            throw new UsernameNotUniqueException("Role name must be unique.");
        }

        roles.add(role);
    }

    @Override
    public void update(Role role) throws UsernameNotUniqueException {
        Role result = Iterables.tryFind(roles, r -> role.getName().equals(r.getName()) && role.getId() == r.getId()).orNull();

        if(result != null) {
            throw new UsernameNotUniqueException("Role name must be unique");
        }

        int index = Iterables.indexOf(roles, r -> role.getId() == r.getId());

        roles.set(index, role);
    }

    @Override
    public void addPermission(Role role, Permission permission) {
        int index = Iterables.indexOf(roles, r -> role.getId() == r.getId());

        roles.get(index).addPermission(permission);
    }

    @Override
    public void addPermissions(Role role, Set<Permission> permissions) {
        int index = Iterables.indexOf(roles, r -> role.getId() == r.getId());

        roles.get(index).addPermissions(permissions);
    }

    @Override
    public Role removePermission(Role role, Permission permission) {
        return null;
    }
}
