/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package repository.interfaces;

import model.User;

import java.util.List;

public interface UserRepository {
    public List<User> getUsers();
    public User getUserById(int id);
    public User getUserByUsername(String username);
    public void create(User user);
    public void follow(User user, User follower);
    public void unfollow(User user, User follower);
    public List<User> getFollowersById(int id);
    public List<User> getFollowersByUsername(String username);
    public List<User> getFollowingById(int id);
    public List<User> getFollowingByUsername(String username);
    public void update(User user);
}
