/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package repository.JPA;

import model.User;
import repository.interfaces.JPA;
import repository.interfaces.UserRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@Stateless
@JPA
public class UserServiceImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> getUsers() {
        return em.createNamedQuery("user.getAllUsers", User.class).getResultList();
    }

    @Override
    public User getUserById(int id) {
        return em.createNamedQuery("user.getUserById", User.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public User getUserByUsername(String username) {
        return em.createNamedQuery("user.getUserByUsername", User.class).setParameter("username", username).getSingleResult();
    }

    @Override
    public void create(User user) {
        em.persist(user);
    }

    @Override
    public void follow(User user, User follower) {
        user.addFollowing(follower);
        em.merge(user);
    }

    @Override
    public void unfollow(User user, User follower) {
        user.removeFollowing(follower);
        em.merge(user);
    }

    @Override
    public Set<User> getFollowersById(int id) {
        return getUserById(id).getFollowers();
    }

    @Override
    public Set<User> getFollowersByUsername(String username) {
        return getUserByUsername(username).getFollowers();
    }

    @Override
    public Set<User> getFollowingById(int id) {
        return getUserById(id).getFollowing();
    }

    @Override
    public Set<User> getFollowingByUsername(String username) {
        return getUserByUsername(username).getFollowing();
    }

    @Override
    public void update(User user) {
        em.merge(user);
    }
}
