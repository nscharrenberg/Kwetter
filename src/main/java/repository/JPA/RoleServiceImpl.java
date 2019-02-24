/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package repository.JPA;

import exception.UsernameNotUniqueException;
import model.Permission;
import model.Role;
import repository.interfaces.JPA;
import repository.interfaces.RoleRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@Stateless
@JPA
public class RoleServiceImpl implements RoleRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Role> getRoles() {
        return em.createNamedQuery("role.getAllRoles", Role.class).getResultList();
    }

    @Override
    public Role getRoleById(int id) {
        return em.createNamedQuery("role.getRoleById", Role.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public Role getRoleByName(String name) {
        return em.createNamedQuery("role.getRoleByName", Role.class).setParameter("name", name).getSingleResult();
    }

    @Override
    public void create(Role role) throws UsernameNotUniqueException {
        em.persist(role);
    }

    @Override
    public void update(Role role) throws UsernameNotUniqueException {
        em.merge(role);
    }

    @Override
    public void addPermission(Role role, Permission permission) {
        role.addPermission(permission);
        em.merge(role);
    }

    @Override
    public void addPermissions(Role role, Set<Permission> permissions) {
        role.addPermissions(permissions);
        em.merge(role);
    }

    @Override
    public void removePermission(Role role, Permission permission) {
        role.removePermission(permission);
        em.merge(role);
    }
}
