/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package service;

import model.User;

import java.util.List;

public class UserService {

    /**
     * Get all Users
     * @return
     */
    public List<User> getUsers() {
        //todo: implement getUsers method
        return null;
    }

    /**
     * Get a specific user by ID
     * @param id - the id of the user that has to be found.
     * @return
     */
    public User getUserById(int id) {
        //todo: implement getUserById method
        return null;
    }

    /**
     * Get a specific user by username
     * @param username - the username of the user that has to be found.
     * @return
     */
    public User getUserByUsername(String username) {
        //todo: implement getUserByUsername method
        return null;
    }

    /**
     * Create a new USer
     * @param user - the user to be created.
     * @return
     */
    public User create(User user) {
        //todo: implement createUser method
        return null;
    }

    /**
     * Follow a user
     * @param user - the person that is going to follow somebody.
     * @param follower - the person that is being followed.
     */
    public void follow(User user, User follower) {
        //todo: implement follow method - Think about passing 2 params or to use 1 parameter and get the current user from the session.

    }

    /**
     * Unfollow a user
     * @param user - the person that is currently following somebody.
     * @param follower - the person that is being unfollowed.
     */
    public void unfollow(User user, User follower) {
        //todo: implement unfollow method - Think about passing 2 params or to use 1 parameter and get the current user from the session.
    }

    /**
     * Get the followers of a specific User by Id
     * @param id - The id from the User you want to get the followers from
     * @return
     */
    public List<User> getFollowersById(int id) {
        User user = getUserById(id);

        //todo: implement getFollowersById method
        return null;
    }

    /**
     * Get the followers of a specific User by Username
     * @param username - The username from the User you want to get the followers from
     * @return
     */
    public List<User> getFollowersByUsername(String username) {
        User user = getUserByUsername(username);

        //todo: implement getFollowersByUsername method
        return null;
    }


    /**
     * Get a specific User by Id and show the Users it's following.
     * @param id - the id from the User you want to get the Users it's following from
     * @return
     */
    public List<User> getFollowingById(int id) {
        User user = getUserById(id);

        //todo: implement getFollowingById method
        return null;
    }

    /**
     * Get a specific User by Username and show the Users it's following.
     * @param username - the username from the User you want to get the Users it's following from
     * @return
     */
    public List<User> getFollowingByUsername(String username) {
        User user = getUserByUsername(username);

        //todo: implement getFollowingByUsername method
        return null;
    }

    /**
     * Update a specific user
     * @param user - update the user information of a specific user.
     * @return
     */
    public User updateUser(User user) {
        //todo: implement updateUser method
        return null;
    }
}
