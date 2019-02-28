package repository.jpa;

import domain.Permission;
import exceptions.NameNotUniqueException;
import repository.interfaces.JPA;
import repository.interfaces.PermissionRepository;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@JPA
@Stateless
public class PermissionServiceJPAImpl implements PermissionRepository {

    @PersistenceContext(unitName = "kwetterDB")
    private EntityManager em;

    @Override
    public List<Permission> all() {
        return em.createNamedQuery("permission.getAllPermissions", Permission.class).getResultList();
    }

    @Override
    public Permission getById(int id) {
        return em.createNamedQuery("permission.getPermissionById", Permission.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public Permission getByName(String name) {
        return em.createNamedQuery("permission.getPermissionByName", Permission.class).setParameter("name", name).getSingleResult();
    }

    @Override
    public Permission create(Permission permission) {
        em.persist(permission);

        return permission;
    }

    @Override
    public Permission update(Permission permission) {
        return em.merge(permission);
    }

    @Override
    public boolean delete(Permission permission) {
        em.remove(permission);
        return true;
    }
}
