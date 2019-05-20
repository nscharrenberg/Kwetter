package com.nscharrenberg.kwetter.service;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.nscharrenberg.kwetter.authentication.PasswordAuthentication;
import com.nscharrenberg.kwetter.domain.Permission;
import com.nscharrenberg.kwetter.domain.Role;
import com.nscharrenberg.kwetter.domain.User;
import com.nscharrenberg.kwetter.repository.interfaces.JPA;
import com.nscharrenberg.kwetter.repository.interfaces.UserRepository;
import com.nscharrenberg.kwetter.responses.StatusCodes;
import com.nscharrenberg.kwetter.responses.ObjectResponse;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UserService implements Serializable {

    @Inject @JPA
    private UserRepository ur;

    @Inject
    private RoleService rr;

    /**
     * Get all Users
     * @return a list of users
     */
    public ObjectResponse<List<User>> all() {
        List<User> users = ur.all();
        return new ObjectResponse<>(StatusCodes.OK, users.size() + " permissions loaded", users);
    }

    /**
     * Get a user by its id
     * @param id - the id of the user
     * @return a user
     */
    public ObjectResponse<User> getById(int id) {
        if(id <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        User user = ur.getById(id);

        if(user == null) {
            return new ObjectResponse<>(StatusCodes.NOT_FOUND, "User not found");
        }

        return new ObjectResponse<>(StatusCodes.OK, "User with username: " + user.getUsername() + " found", user);
    }

    /**
     * Get a user by its name
     * @param username - the username of the user
     * @return a user
     */
    public ObjectResponse<User> getByUsername(String username) {
        if(username.isEmpty()) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "username can not be empty");
        }

        User user = ur.getByUsername(username);

        if(user == null) {
            return new ObjectResponse<>(StatusCodes.NOT_FOUND, "User not found");
        }

        return new ObjectResponse<>(StatusCodes.OK, "User with username: " + user.getUsername() + " found", user);
    }

    /**
     * Get a user by its email
     * @param email - the email of the user
     * @return a user
     */
    public ObjectResponse<User> getByEmail(String email) {
        if(email.isEmpty()) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "email can not be empty");
        }

        User user = ur.getByEmail(email);

        if(user == null) {
            return new ObjectResponse<>(StatusCodes.NOT_FOUND, "User not found");
        }

        return new ObjectResponse<>(StatusCodes.OK, "User with username: " + user.getUsername() + " found", user);
    }

    /**
     * Create a new user
     * @param user - the user information
     * @return the newly created user
     */
    public ObjectResponse<User> create(User user) {
        List<String> errorsMessages = new ArrayList<>();

        if(user.getUsername().isEmpty() || user.getUsername() == null) errorsMessages.add("username can not be empty");
        if(user.getEmail().isEmpty() || user.getEmail() == null) errorsMessages.add("email can not be empty");
        if(user.getFirstname().isEmpty() || user.getFirstname() == null) errorsMessages.add("firstname can not be empty");
        if(user.getLastname().isEmpty() || user.getLastname() == null) errorsMessages.add("lastname can not be empty");
        if(user.getBiography().isEmpty() || user.getBiography() == null) errorsMessages.add("biography can not be empty");
        if(user.getPassword().isEmpty() || user.getPassword() == null) errorsMessages.add("password can not be empty");
        if(user.getWebsite().isEmpty() || user.getWebsite() == null) errorsMessages.add("website can not be empty");

        if(errorsMessages.size() > 0) {
            Gson gson = new Gson();
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, gson.toJson(errorsMessages));
        }

        ObjectResponse<User> getByUsernameResponse = getByUsername(user.getUsername());
        if(getByUsernameResponse.getObject() != null) {
            return new ObjectResponse<>(StatusCodes.CONFLICT, "User with username " + user.getUsername() + " already exists.");
        }

        ObjectResponse<User> getByEmailResponse = getByEmail(user.getEmail());

        if(getByEmailResponse.getObject() != null) {
            return new ObjectResponse<>(StatusCodes.CONFLICT, "User with email " + user.getEmail() + " already exists.");
        }

        if(user.getBiography() != null) {
            if(user.getBiography().length() > 160) {
                return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Biography can not be longer then 160 characters.");
            }
        }

        ObjectResponse<Role> getRoleByNameResponse = rr.getByName("member");

        if(getRoleByNameResponse.getObject() == null) {
            return new ObjectResponse<>(getRoleByNameResponse.getCode(), getRoleByNameResponse.getMessage());
        }

        user.setRole(getRoleByNameResponse.getObject());
        user.setPassword(PasswordAuthentication.hash(user.getPassword()));
        User created = ur.create(user);

        if(created != null) {
            return new ObjectResponse<>(StatusCodes.CREATED, "User with name: " + user.getUsername() + " created", user);
        } else {
            return new ObjectResponse<>(StatusCodes.INTERNAL_SERVER_ERROR, "Could not create a new user due to an unknown error");
        }
    }

    /**
     * Update an existing user
     * @param user - the new user information with an existing user id
     * @return the updated user
     */
    public ObjectResponse<User> update(User user) {
        List<String> errorsMessages = new ArrayList<>();
        if(user.getId() <= 0) errorsMessages.add("Invalid ID");
        if(user.getUsername().isEmpty() || user.getUsername() == null) errorsMessages.add("username can not be empty");
        if(user.getEmail().isEmpty() || user.getEmail() == null) errorsMessages.add("email can not be empty");
        if(user.getFirstname().isEmpty() || user.getFirstname() == null) errorsMessages.add("firstname can not be empty");
        if(user.getLastname().isEmpty() || user.getLastname() == null) errorsMessages.add("lastname can not be empty");
        if(user.getBiography().isEmpty() || user.getBiography() == null) errorsMessages.add("biography can not be empty");
        if(user.getWebsite().isEmpty() || user.getWebsite() == null) errorsMessages.add("website can not be empty");
        if(user.getPassword().isEmpty() || user.getPassword() == null) errorsMessages.add("password can not be empty");

        if(errorsMessages.size() > 0) {
            Gson gson = new Gson();
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, gson.toJson(errorsMessages));
        }

        ObjectResponse<User> getByIdResponse = getById(user.getId());

        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        if(!user.getRole().equals(getByIdResponse.getObject().getRole())) {
            return new ObjectResponse<>(StatusCodes.FORBIDDEN, "You can not change the user's role!");
        }

        if(user.getBiography() != null) {
            if(user.getBiography().length() > 160) {
                return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Biography can not be longer then 160 characters.");
            }
        }

        ObjectResponse<User> getByUsernameResponse = getByUsername(user.getUsername());
        if(getByUsernameResponse.getObject() != null && getByUsernameResponse.getObject().getId() != user.getId()) {
            return new ObjectResponse<>(StatusCodes.CONFLICT, "User with username " + user.getUsername() + " already exists.");
        }

        ObjectResponse<User> getByEmailResponse = getByEmail(user.getEmail());

        if(getByEmailResponse.getObject() != null && getByEmailResponse.getObject().getId() != user.getId()) {
            return new ObjectResponse<>(StatusCodes.CONFLICT, "User with email " + user.getEmail() + " already exists.");
        }


        if(!user.getPassword().replaceAll(" ", "").isEmpty()) {
            if(!PasswordAuthentication.verify(user.getPassword(), getByIdResponse.getObject().getPassword()) && !getByIdResponse.getObject().getPassword().equals(user.getPassword())) {
                user.setPassword(PasswordAuthentication.hash(user.getPassword()));
            }
        } else {
            user.setPassword(getByEmailResponse.getObject().getPassword());
        }

        User result = ur.update(user);
        if(result != null) {
            return new ObjectResponse<>(StatusCodes.OK, "User with name: " + user.getUsername() + " updated", user);
        } else {
            return new ObjectResponse<>(StatusCodes.INTERNAL_SERVER_ERROR, "Could not update an existing user due to an unknown error");
        }
    }

    /**
     * Delete an existing user
     * @param user - the user to be deleted
     * @return a boolean wether or not the user is deleted.
     */
    public ObjectResponse<User> delete(User user) {
        if(user.getId() <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        ObjectResponse<User> getByIdResponse = getById(user.getId());

        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        boolean deleted = ur.delete(user);
        if(deleted) {
            return new ObjectResponse<>(StatusCodes.OK, "User with username " + user.getUsername() + " has been deleted");
        } else {
            return new ObjectResponse<>(StatusCodes.INTERNAL_SERVER_ERROR, "User with username " + user.getUsername() + "could not be deleted due to an unknown error");
        }
    }

    /**
     * follow a user
     * @param user - the user that wants to follow another user
     * @param toFollow - the user that is being followed
     * @return the user
     */
    public ObjectResponse<User> follow(User user, User toFollow) {
        if(user.getId() <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        if(toFollow.getId() <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid ID for User you are trying to follow");
        }

        if(user.getId() == toFollow.getId()) {
            return new ObjectResponse<>(StatusCodes.FORBIDDEN, "You can't follow yourself!");
        }

        ObjectResponse<User> getUserByIdResponse = getById(user.getId());

        if(getUserByIdResponse.getObject() == null) {
            return getUserByIdResponse;
        }

        ObjectResponse<User> getToFollowByIdResponse = getById(toFollow.getId());

        if(getToFollowByIdResponse.getObject() == null) {
            return getToFollowByIdResponse;
        }

        if(getUserByIdResponse.getObject().getFollowing().contains(toFollow) || getToFollowByIdResponse.getObject().getFollowers().contains(user)) {
            return new ObjectResponse<>(StatusCodes.FORBIDDEN, user.getUsername() + " is already following " + toFollow.getUsername());
        }

        User result = ur.follow(getUserByIdResponse.getObject(), getToFollowByIdResponse.getObject());
        if(result != null) {
            return new ObjectResponse<>(StatusCodes.OK, "User with username: " + user.getUsername() + " is now following User with username: " + toFollow.getUsername(), result);
        } else {
            return new ObjectResponse<>(StatusCodes.INTERNAL_SERVER_ERROR, "Could not follow User due to an unknown error");
        }
    }

    /**
     * unfollow a user
     * @param user - the user that wants to unfollow another user
     * @param toUnfollow - the user that needs to be unfollowed
     * @return the user
     */
    public ObjectResponse<User> unfollow(User user, User toUnfollow) {
        if(user.getId() <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        if(toUnfollow.getId() <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid ID for User you are trying to unfollow");
        }

        ObjectResponse<User> getUserByIdResponse = getById(user.getId());
        ObjectResponse<User> getToUnFollowByIdResponse = getById(toUnfollow.getId());

        if(getUserByIdResponse.getObject() == null) {
            return getUserByIdResponse;
        }

        if(getToUnFollowByIdResponse.getObject() == null) {
            return getToUnFollowByIdResponse;
        }

        if(!ur.getById(user.getId()).getFollowing().contains(toUnfollow) || !ur.getById(toUnfollow.getId()).getFollowers().contains(user)) {
            return new ObjectResponse<>(StatusCodes.FORBIDDEN, user.getUsername() + " is not following " + toUnfollow.getUsername());
        }

        User result = ur.unfollow(getUserByIdResponse.getObject(), getToUnFollowByIdResponse.getObject());
        if(result != null) {
            return new ObjectResponse<>(StatusCodes.OK, "User with username: " + user.getUsername() + " is not following User with username: " + toUnfollow.getUsername() + " anymore", result);
        } else {
            return new ObjectResponse<>(StatusCodes.INTERNAL_SERVER_ERROR, "Could not unfollow User due to an unknown error");
        }
    }

    /**
     * Login as a user
     * @param username - the username if the user
     * @param password - the password of the user
     * @return a User
     */
    public ObjectResponse<User> login(String username, String password) {
        if(username.isEmpty()) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Username can not be empty");
        }

        if (password.isEmpty()) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Password can not be empty");
        }

        ObjectResponse<User> findByUsernameResponse = getByUsername(username);

        if(findByUsernameResponse.getObject() != null) {
            if(PasswordAuthentication.verify(password, findByUsernameResponse.getObject().getPassword())) {
                return new ObjectResponse<>(StatusCodes.OK, String.format("You are logged in as %s", findByUsernameResponse.getObject().getEmail()), findByUsernameResponse.getObject());
            }
        }

        return new ObjectResponse<>(StatusCodes.UNAUTHORIZED, "Wrong username or password");
    }

    /**
     * Change the role of a user
     * @param user - the user
     * @param role - the role you want to add
     * @return the user
     */
    public ObjectResponse<User> changeRole(User user, Role role) {
        if(user.getId() <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid User ID");
        }

        if(role.getId() <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid Role ID");
        }

        ObjectResponse<User> getByIdResponse = getById(user.getId());

        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        ObjectResponse<Role> getRoleByIdResponse = rr.getById(role.getId());
        if(getRoleByIdResponse.getObject() == null) {
            return new ObjectResponse<>(getRoleByIdResponse.getCode(), getRoleByIdResponse.getMessage());
        }

        User result =  ur.changeRole(user, role);

        if(result != null) {
            return new ObjectResponse<>(StatusCodes.OK, "User with username: " + user.getUsername() + " now has the role: " + role.getName(), result);
        } else {
            return new ObjectResponse<>(StatusCodes.INTERNAL_SERVER_ERROR, "Could not add role to a User due to an unknown error");
        }
    }

    public ObjectResponse<Boolean> hasPermission(User user, String permission) {
        if(user.getId() <= 0) {
            return new ObjectResponse<>(StatusCodes.UNAUTHORIZED, String.format("User does not have permission to %s", permission), false);
        }

        if(permission.isEmpty()) {
            return new ObjectResponse<>(StatusCodes.UNAUTHORIZED, String.format("User does not have permission to %s", permission), false);
        }

        Permission p = Iterables.tryFind(user.getRole().getPermissions(), new Predicate<Permission>() {
            @Override
            public boolean apply(@Nullable Permission input) {
                return input.getName().contains(permission);
            }
        }).orNull();

        if(p == null) {
            return new ObjectResponse<>(StatusCodes.UNAUTHORIZED, String.format("User does not have permission to %s", permission), false);
        }

        return new ObjectResponse<>(StatusCodes.OK, String.format("User has permission to %s", permission), true);
    }

    public ObjectResponse<Boolean> isAdmin(User user) {
        if(user.getId() <= 0) {
            return new ObjectResponse<>(StatusCodes.UNAUTHORIZED, "User is not Admin");
        }

        Role r = user.getRole();

        if(r.getName().contains("admin")) {
            return new ObjectResponse<>(StatusCodes.OK, String.format("User %s is admin", user.getUsername()), true);
        }

        return new ObjectResponse<>(StatusCodes.UNAUTHORIZED, String.format("User %s does not have admin privileges", user.getUsername()), false);
    }
}
