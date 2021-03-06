package com.nscharrenberg.kwetter.repository.jpa;

import com.nscharrenberg.kwetter.domain.Role;
import com.nscharrenberg.kwetter.domain.User;
import com.nscharrenberg.kwetter.repository.interfaces.JPA;
import com.nscharrenberg.kwetter.repository.interfaces.UserRepository;
import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.QueryHints;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@JPA
@Stateless
public class UserServiceJPAImpl implements UserRepository {

    @PersistenceContext(unitName = "kwetterDB")
    private EntityManager em;

    @Override
    public List<User> all() {
        return em.createNamedQuery("user.getAllUsers", User.class).getResultList();
    }

    @Override
    public User getById(int id) {
        try {
            return em.createNamedQuery("user.getUserById", User.class).setHint(QueryHints.CACHE_USAGE, CacheUsage.DoNotCheckCache).setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getByUsername(String username) {
        try {
            return em.createNamedQuery("user.getUserByUsername", User.class).setHint(QueryHints.CACHE_USAGE, CacheUsage.DoNotCheckCache).setParameter("username", username).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getByEmail(String email) {
        try {
            return em.createNamedQuery("user.getUserByEmail", User.class).setHint(QueryHints.CACHE_USAGE, CacheUsage.DoNotCheckCache).setParameter("email", email).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User create(User user) {
        try {
            em.persist(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User update(User user) {
        try {
            return em.merge(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(User user) {
        try {
            if(!em.contains(user)) {
                user = em.merge(user);
            }

            em.remove(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User follow(User user, User toFollow) {
        try {
            user.addFollowing(toFollow);
            return em.merge(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User unfollow(User user, User toUnfollow) {
        try {
            user.removeFollowing(toUnfollow);
            return em.merge(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User changeRole(User user, Role role) {
        try {
            user.setRole(role);
            return em.merge(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User login(String username, String encryptedPassword) {
        try {
            return em.createNamedQuery("user.login", User.class).setParameter("username", username).setParameter("password", encryptedPassword).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
