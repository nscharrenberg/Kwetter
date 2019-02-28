package repository.jpa;

import domain.Role;
import domain.User;
import exceptions.NameNotUniqueException;
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
        return em.createNamedQuery("user.getUserById", User.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public User getByUsername(String username) {
        return em.createNamedQuery("user.getUserByUsername", User.class).setParameter("username", username).getSingleResult();
    }

    @Override
    public User getByEmail(String email) {
        return em.createNamedQuery("user.getUserByEmail", User.class).setParameter("email", email).getSingleResult();
    }

    @Override
    public User create(User user) throws NameNotUniqueException, ClassNotFoundException {
        em.persist(user);
        return user;
    }

    @Override
    public User update(User user) throws NameNotUniqueException {
        return em.merge(user);
    }

    @Override
    public boolean delete(User user) throws ClassNotFoundException {
        em.remove(user);
        return true;
    }

    @Override
    public User follow(User user, User toFollow) throws ClassNotFoundException, NameNotUniqueException {
        user.addFollowing(toFollow);
        return em.merge(user);
    }

    @Override
    public User unfollow(User user, User toUnfollow) throws ClassNotFoundException, NameNotUniqueException {
        user.removeFollowing(toUnfollow);
        return em.merge(user);
    }

    @Override
    public boolean login(String username, String password) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);

        return query.getSingleResult() != null;
    }

    @Override
    public User changeRole(User user, Role role) throws ClassNotFoundException, NameNotUniqueException {
        user.setRole(role);
        return em.merge(user);
    }
}
