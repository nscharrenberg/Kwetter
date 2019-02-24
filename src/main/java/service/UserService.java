/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package service;

import exception.StringToLongException;
import exception.UsernameNotUniqueException;
import model.Role;
import model.User;
import repository.interfaces.JPA;
import repository.interfaces.RoleRepository;
import repository.interfaces.UserRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@Stateless
public class UserService {

    @Inject
    @Default
    private UserRepository ur;

    @Inject
    @Default
    private RoleRepository rr;

    public UserService() {
    }

    /**
     * Get all Users
     * @return
     */
    public List<User> getUsers() {
        return ur.getUsers();
    }

    /**
     * Get a specific user by ID
     * @param id - the id of the user that has to be found.
     * @return
     */
    public User getUserById(int id) {
        return ur.getUserById(id);
    }

    /**
     * Get a specific user by username
     * @param username - the username of the user that has to be found.
     * @return
     */
    public User getUserByUsername(String username) {
        return ur.getUserByUsername(username);
    }

    /**
     * Create a new USer
     * @param user - the user to be created.
     * @return
     */
    public void create(User user) throws Exception {
        Role role = rr.getRoleById(user.getRole().getId());

        if(role == null) {
            throw new Exception("Role not found, during registration.");
        }

        ur.create(user);
    }

    /**
     * Follow a user
     * @param user - the person that is going to follow somebody.
     * @param follower - the person that is being followed.
     */
    public void follow(User user, User follower) {
        ur.follow(user, follower);
    }

    /**
     * Unfollow a user
     * @param user - the person that is currently following somebody.
     * @param follower - the person that is being unfollowed.
     */
    public void unfollow(User user, User follower) {
        ur.unfollow(user, follower);
    }

    /**
     * Get the followers of a specific User by Id
     * @param id - The id from the User you want to get the followers from
     * @return
     */
    public Set<User> getFollowersById(int id) {
        return ur.getFollowersById(id);
    }

    /**
     * Get the followers of a specific User by Username
     * @param username - The username from the User you want to get the followers from
     * @return
     */
    public Set<User> getFollowersByUsername(String username) {
        return ur.getFollowersByUsername(username);
    }


    /**
     * Get a specific User by Id and show the Users it's following.
     * @param id - the id from the User you want to get the Users it's following from
     * @return
     */
    public Set<User> getFollowingById(int id) {
        return ur.getFollowingById(id);
    }

    /**
     * Get a specific User by Username and show the Users it's following.
     * @param username - the username from the User you want to get the Users it's following from
     * @return
     */
    public Set<User> getFollowingByUsername(String username) {
        return ur.getFollowingByUsername(username);
    }

    /**
     * Update a specific user
     * @param user - update the user information of a specific user.
     * @return
     */
    public void update(User user) throws Exception {
        Role role = rr.getRoleById(user.getRole().getId());

        if(role == null) {
            throw new Exception("Role not found, during registration.");
        }

        ur.update(user);
    }
}
