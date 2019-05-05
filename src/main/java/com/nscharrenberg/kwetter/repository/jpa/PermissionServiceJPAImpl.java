package com.nscharrenberg.kwetter.repository.jpa;

import com.nscharrenberg.kwetter.domain.Permission;
import com.nscharrenberg.kwetter.exceptions.NameNotUniqueException;
import com.nscharrenberg.kwetter.repository.interfaces.JPA;
import com.nscharrenberg.kwetter.repository.interfaces.PermissionRepository;

import javax.ejb.*;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.Transactional;
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
        try {
            return em.createNamedQuery("permission.getPermissionById", Permission.class).setParameter("id", id).getSingleResult();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Permission getByName(String name) {
        try {
            return em.createNamedQuery("permission.getPermissionByName", Permission.class).setParameter("name", name).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Permission create(Permission permission) {
        try {
            em.persist(permission);
            return permission;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Permission update(Permission permission) {
        try {
            return em.merge(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(Permission permission) {
        try {
            if(!em.contains(permission)) {
                permission = em.merge(permission);
            }
            em.remove(permission);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
