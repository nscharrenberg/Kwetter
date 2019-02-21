/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package repository.JPA;

import model.User;
import repository.interfaces.JPA;
import repository.interfaces.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@JPA
public class UserServiceImpl implements UserRepository {
    
    private EntityManager em;

    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public void create(User user) {

    }

    @Override
    public void follow(User user, User follower) {

    }

    @Override
    public void unfollow(User user, User follower) {

    }

    @Override
    public List<User> getFollowersById(int id) {
        return null;
    }

    @Override
    public List<User> getFollowersByUsername(String username) {
        return null;
    }

    @Override
    public List<User> getFollowingById(int id) {
        return null;
    }

    @Override
    public List<User> getFollowingByUsername(String username) {
        return null;
    }

    @Override
    public void update(User user) {
            }
}
