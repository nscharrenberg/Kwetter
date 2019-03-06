package repository.jpa;

import domain.Role;
import domain.User;
import exceptions.NameNotUniqueException;
import repository.interfaces.JPA;
import repository.interfaces.UserRepository;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.Produces;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@JPA
@Stateless
public class UserServiceJPAImpl implements UserRepository {

    @PersistenceContext(unitName = "kwetterDB")
    private EntityManager em;

    @Override
    public List<User> all() {
        EntityGraph eg = em.getEntityGraph("user-entity-graph");
        return em.createNamedQuery("user.getAllUsers", User.class).setHint("javax.persistence.fetchgraph", eg).getResultList();
    }

    @Override
    public User getById(int id) {
        try {
            return em.find(User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getByUsername(String username) {
        try {
            return em.find(User.class, username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getByEmail(String email) {
        try {
            return em.find(User.class, email);
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
    public boolean login(String username, String password) {
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            return query.getSingleResult() != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
}
