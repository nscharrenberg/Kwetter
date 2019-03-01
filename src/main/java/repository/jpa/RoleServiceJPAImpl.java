package repository.jpa;

import domain.Permission;
import domain.Role;
import repository.interfaces.JPA;
import repository.interfaces.RoleRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@JPA
@Transactional
@Stateless
public class RoleServiceJPAImpl implements RoleRepository {

    @PersistenceContext(unitName = "kwetterDB")
    private EntityManager em;

    @Override
    public List<Role> all() {
        return em.createQuery("from Role ", Role.class).getResultList();
    }

    @Override
    public Role getById(int id) {
        try {
            return em.find(Role.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Role getByName(String name) {
        try {
            return em.find(Role.class, name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Role create(Role role) {
        try {
            em.persist(role);
            return role;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Role update(Role role) {
        try {
            return em.merge(role);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(Role role) {
        try {
            em.remove(role);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Role addPermission(Role role, Permission permission) {
        try {
            role.addPermission(permission);
            return em.merge(role);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Role removePermission(Role role, Permission permission) {
        try {
            role.removePermission(permission);
            return em.merge(role);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
