package repository.jpa;

import domain.Role;
import domain.User;
import repository.interfaces.JPA;
import repository.interfaces.UserRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
            return em.createNamedQuery("user.getUserById", User.class).setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getByUsername(String username) {
        try {
            return em.createNamedQuery("user.getUserByUsername", User.class).setParameter("username", username).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getByEmail(String email) {
        try {
            return em.createNamedQuery("user.getUserByEmail", User.class).setParameter("email", email).getSingleResult();
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
        List<User> users = em.createNamedQuery("user.login", User.class).setParameter("username", username).setParameter("password", encryptedPassword).getResultList();
        User found = null;

        if(!users.isEmpty()) {
            found = users.get(0);
        }

        return found;
    }
}
