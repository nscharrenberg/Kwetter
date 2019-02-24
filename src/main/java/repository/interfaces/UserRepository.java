/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package repository.interfaces;

import exception.StringToLongException;
import exception.UsernameNotUniqueException;
import model.User;

import java.util.List;
import java.util.Set;

public interface UserRepository {
    List<User> getUsers();
    User getUserById(int id);
    User getUserByUsername(String username);
    void create(User user) throws UsernameNotUniqueException, StringToLongException;
    void follow(User user, User follower);
    void unfollow(User user, User follower);
    Set<User> getFollowersById(int id);
    Set<User> getFollowersByUsername(String username);
    Set<User> getFollowingById(int id);
    Set<User> getFollowingByUsername(String username);
    void update(User user) throws UsernameNotUniqueException, StringToLongException;
}
