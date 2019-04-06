package service;

import authentication.PasswordAuthentication;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import domain.Permission;
import domain.Role;
import domain.User;
import exceptions.*;
import org.checkerframework.checker.nullness.qual.Nullable;
import repository.interfaces.JPA;
import repository.interfaces.UserRepository;
import responses.HttpStatusCodes;
import responses.ObjectResponse;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@Stateless
public class UserService {

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
        return new ObjectResponse<>(HttpStatusCodes.OK, users.size() + " permissions loaded", users);
    }

    /**
     * Get a user by its id
     * @param id - the id of the user
     * @return a user
     */
    public ObjectResponse<User> getById(int id) {
        if(id <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        User user = ur.getById(id);

        if(user == null) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "User not found");
        }

        return new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user.getUsername() + " found", user);
    }

    /**
     * Get a user by its name
     * @param username - the username of the user
     * @return a user
     */
    public ObjectResponse<User> getByUsername(String username) {
        if(username.isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "username can not be empty");
        }

        User user = ur.getByUsername(username);

        if(user == null) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "User not found");
        }

        return new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user.getUsername() + " found", user);
    }

    /**
     * Get a user by its email
     * @param email - the email of the user
     * @return a user
     */
    public ObjectResponse<User> getByEmail(String email) {
        if(email.isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "email can not be empty");
        }

        User user = ur.getByEmail(email);

        if(user == null) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "User not found");
        }

        return new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user.getUsername() + " found", user);
    }

    /**
     * Create a new user
     * @param user - the user information
     * @return the newly created user
     */
    public ObjectResponse<User> create(User user) {
        if(user.getUsername().isEmpty() || user.getUsername() == null) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "username can not be empty");
        }

        if(user.getEmail().isEmpty() || user.getEmail() == null) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "email can not be empty");
        }

        ObjectResponse<User> getByUsernameResponse = getByUsername(user.getUsername());
        if(getByUsernameResponse.getObject() != null) {
            return new ObjectResponse<>(HttpStatusCodes.CONFLICT, "User with username " + user.getUsername() + " already exists.");
        }

        ObjectResponse<User> getByEmailResponse = getByEmail(user.getEmail());

        if(getByEmailResponse.getObject() != null) {
            return new ObjectResponse<>(HttpStatusCodes.CONFLICT, "User with email " + user.getEmail() + " already exists.");
        }

        if(user.getBiography() != null) {
            if(user.getBiography().length() > 160) {
                return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Biography can not be longer then 160 characters.");
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
            return new ObjectResponse<>(HttpStatusCodes.CREATED, "User with name: " + user.getUsername() + " created", user);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not create a new user due to an unknown error");
        }
    }

    /**
     * Update an existing user
     * @param user - the new user information with an existing user id
     * @return the updated user
     */
    public ObjectResponse<User> update(User user) {
        if(user.getUsername().isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "username can not be empty");
        }

        if(user.getEmail().isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "email can not be empty");
        }

        if(user.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        ObjectResponse<User> getByIdResponse = getById(user.getId());

        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        if(user.getBiography() != null) {
            if(user.getBiography().length() > 160) {
                return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Biography can not be longer then 160 characters.");
            }
        }

        ObjectResponse<User> getByUsernameResponse = getByUsername(user.getUsername());
        if(getByUsernameResponse.getObject() != null && getByUsernameResponse.getObject().getId() != user.getId()) {
            return new ObjectResponse<>(HttpStatusCodes.CONFLICT, "User with username " + user.getUsername() + " already exists.");
        }

        ObjectResponse<User> getByEmailResponse = getByEmail(user.getEmail());

        if(getByEmailResponse.getObject() != null && getByEmailResponse.getObject().getId() != user.getId()) {
            return new ObjectResponse<>(HttpStatusCodes.CONFLICT, "User with email " + user.getEmail() + " already exists.");
        }

        User result = ur.update(user);
        if(result != null) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "User with name: " + user.getUsername() + " updated", user);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not update an existing user due to an unknown error");
        }
    }

    /**
     * Delete an existing user
     * @param user - the user to be deleted
     * @return a boolean wether or not the user is deleted.
     */
    public ObjectResponse<User> delete(User user) {
        if(user.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        ObjectResponse<User> getByIdResponse = getById(user.getId());

        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        boolean deleted = ur.delete(user);
        if(deleted) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "User with username " + user.getUsername() + " has been deleted");
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "User with username " + user.getUsername() + "could not be deleted due to an unknown error");
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
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        if(toFollow.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID for User you are trying to follow");
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
            return new ObjectResponse<>(HttpStatusCodes.FORBIDDEN, user.getUsername() + " is already following " + toFollow.getUsername());
        }

        User result = ur.follow(getUserByIdResponse.getObject(), getToFollowByIdResponse.getObject());
        if(result != null) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user.getUsername() + " is now following User with username: " + toFollow.getUsername(), result);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not follow User due to an unknown error");
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
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        if(toUnfollow.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID for User you are trying to unfollow");
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
            return new ObjectResponse<>(HttpStatusCodes.FORBIDDEN, user.getUsername() + " is not following " + toUnfollow.getUsername());
        }

        User result = ur.unfollow(getUserByIdResponse.getObject(), getToUnFollowByIdResponse.getObject());
        if(result != null) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user.getUsername() + " is not following User with username: " + toUnfollow.getUsername() + " anymore", result);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not unfollow User due to an unknown error");
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
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Username can not be empty");
        }

        if (password.isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Password can not be empty");
        }

        User user = ur.login(username, PasswordAuthentication.hash(password));

        if(user != null) {
            return new ObjectResponse<>(HttpStatusCodes.OK, String.format("You are logged in as %s", user.getUsername()), user);
        }

        return new ObjectResponse<>(HttpStatusCodes.UNAUTHORIZED, "Wrong username or password");
    }

    /**
     * Change the role of a user
     * @param user - the user
     * @param role - the role you want to add
     * @return the user
     * @throws NotFoundException
     */
    public ObjectResponse<User> changeRole(User user, Role role) {
        if(user.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid User ID");
        }

        if(role.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid Role ID");
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
            return new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user.getUsername() + " now has the role: " + role.getName(), result);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not add role to a User due to an unknown error");
        }
    }

    public ObjectResponse<Boolean> hasPermission(User user, String permission) {
        if(user.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.UNAUTHORIZED, String.format("User does not have permission to %s", permission), false);
        }

        if(permission.isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.UNAUTHORIZED, String.format("User does not have permission to %s", permission), false);
        }

        Permission p = Iterables.tryFind(user.getRole().getPermissions(), new Predicate<Permission>() {
            @Override
            public boolean apply(@Nullable Permission input) {
                return input.getName().contains(permission);
            }
        }).orNull();

        if(p == null) {
            return new ObjectResponse<>(HttpStatusCodes.UNAUTHORIZED, String.format("User does not have permission to %s", permission), false);
        }

        return new ObjectResponse<>(HttpStatusCodes.OK, String.format("User has permission to %s", permission), true);
    }

    public ObjectResponse<Boolean> isAdmin(User user) {
        if(user.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.UNAUTHORIZED, "User is not Admin");
        }

        Role r = user.getRole();

        if(r.getName().contains("admin")) {
            return new ObjectResponse<>(HttpStatusCodes.OK, String.format("User %s is admin", user.getUsername()), true);
        }

        return new ObjectResponse<>(HttpStatusCodes.UNAUTHORIZED, String.format("User %s does not have admin privileges", user.getUsername()), false);
    }
}
