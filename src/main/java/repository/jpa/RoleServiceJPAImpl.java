package repository.jpa;

import domain.Permission;
import domain.Role;
import repository.interfaces.JPA;
import repository.interfaces.RoleRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@JPA
@Stateless
public class RoleServiceJPAImpl implements RoleRepository {

    @PersistenceContext(unitName = "kwetterDB")
    private EntityManager em;

    @Override
    public List<Role> all() {
        return em.createNamedQuery("role.getAllRoles", Role.class).getResultList();
    }

    @Override
    public Role getById(int id) {
        return em.createNamedQuery("role.getRoleById", Role.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public Role getByName(String name) {
        return em.createNamedQuery("role.getRoleByName", Role.class).setParameter("name", name).getSingleResult();
    }

    @Override
    public Role create(Role role) {
        em.persist(role);
        return role;
    }

    @Override
    public Role update(Role role) {
        return em.merge(role);
    }

    @Override
    public boolean delete(Role role) {
        em.remove(role);
        return true;
    }

    @Override
    public Role addPermission(Role role, Permission permission) {
        role.addPermission(permission);
        return em.merge(role);
    }

    @Override
    public Role removePermission(Role role, Permission permission) {
        role.removePermission(permission);
        return em.merge(role);
    }
}
