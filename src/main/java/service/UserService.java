package service;

import domain.Role;
import domain.User;
import exceptions.*;
import repository.interfaces.JPA;
import repository.interfaces.UserRepository;

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
    public List<User> all() {
        return ur.all();
    }

    /**
     * Get a user by its id
     * @param id - the id of the user
     * @return a user
     * @throws InvalidContentException
     * @throws NotFoundException
     */
    public User getById(int id) throws InvalidContentException, NotFoundException {
        if(id <= 0) {
            throw new InvalidContentException("Invalid ID");
        }

        User user = ur.getById(id);

        if(user == null) {
            throw new NotFoundException("User not found");
        }

        return user;
    }

    /**
     * Get a user by its name
     * @param username - the username of the user
     * @return a user
     * @throws InvalidContentException
     * @throws NotFoundException
     */
    public User getByUsername(String username) throws InvalidContentException, NotFoundException {
        if(username.isEmpty()) {
            throw new InvalidContentException("username can not be empty");
        }

        User user = ur.getByUsername(username);

        if(user == null) {
            throw new NotFoundException("User not found");
        }

        return user;
    }

    /**
     * Get a user by its email
     * @param email - the email of the user
     * @return a user
     * @throws InvalidContentException
     * @throws NotFoundException
     */
    public User getByEmail(String email) throws InvalidContentException, NotFoundException {
        if(email.isEmpty()) {
            throw new InvalidContentException("username can not be empty");
        }

        User user = ur.getByEmail(email);

        if(user == null) {
            throw new NotFoundException("User not found");
        }

        return user;
    }

    /**
     * Create a new user
     * @param user - the user information
     * @return the newly created user
     * @throws NameNotUniqueException
     * @throws CreationFailedException
     * @throws NoSuchAlgorithmException
     * @throws InvalidContentException
     */
    public User create(User user) throws NameNotUniqueException, CreationFailedException, InvalidContentException, NoSuchAlgorithmException, NotFoundException {
        if(user.getUsername().isEmpty() || user.getUsername() == null) {
            throw new InvalidContentException("username can not be empty");
        }

        if(user.getEmail().isEmpty() || user.getEmail() == null) {
            throw new InvalidContentException("email can not be empty");
        }

        if(ur.getByUsername(user.getUsername()) != null) {
            throw new NameNotUniqueException("Username already taken");
        }

        user.setRole(rr.getByName("member"));
        user.setPassword(tempSha256Encryption(user.getPassword()));
        User created = ur.create(user);

        if(created != null) {
            return created;
        } else {
            throw new CreationFailedException("Could not create a new user due to an unknown error");
        }
    }

    /**
     * Update an existing user
     * @param user - the new user information with an existing user id
     * @return the updated user
     * @throws NameNotUniqueException
     */
    public User update(User user) throws NameNotUniqueException, InvalidContentException, NotFoundException {
        if(user.getUsername().isEmpty()) {
            throw new InvalidContentException("username can not be empty");
        }

        if(user.getId() <= 0) {
            throw new InvalidContentException("Invalid ID");
        }

        if(ur.getById(user.getId()) == null) {
            throw new NotFoundException("User not found");
        }

        User usernameResult = ur.getByUsername(user.getUsername());
        if(usernameResult != null && usernameResult.getId() != user.getId()) {
            throw new NameNotUniqueException("User with name " + user.getUsername() + " already exists.");
        }

        User emailResult = ur.getByEmail(user.getEmail());
        if(emailResult != null && emailResult.getId() != user.getId()) {
            throw new NameNotUniqueException("User with email " + user.getEmail() + " already exists.");
        }

        return ur.update(user);
    }

    /**
     * Delete an existing user
     * @param user - the user to be deleted
     * @return a boolean wether or not the user is deleted.
     * @throws InvalidContentException
     * @throws NotFoundException
     */
    public boolean delete(User user) throws InvalidContentException, NotFoundException {
        if(user.getId() <= 0) {
            throw new InvalidContentException("Invalid ID");
        }

        if(ur.getById(user.getId()) == null) {
            throw new NotFoundException("User not found");
        }

        return ur.delete(user);
    }

    /**
     * follow a user
     * @param user - the user that wants to follow another user
     * @param toFollow - the user that is being followed
     * @return the user
     * @throws InvalidContentException
     * @throws NotFoundException
     */
    public User follow(User user, User toFollow) throws InvalidContentException, NotFoundException, ActionForbiddenException {
        if(user.getId() <= 0) {
            throw new InvalidContentException("Invalid user ID");
        }

        if(toFollow.getId() <= 0) {
            throw new InvalidContentException("Invalid ID for User you are trying to follow");
        }

        if(ur.getById(user.getId()).getFollowing().contains(toFollow) || ur.getById(toFollow.getId()).getFollowers().contains(user)) {
            throw new ActionForbiddenException(user.getUsername() + " is already following " + toFollow.getUsername());
        }

        if(ur.getById(user.getId()) == null) {
            throw new NotFoundException("User not found");
        }

        if(ur.getById(toFollow.getId()) == null) {
            throw new NotFoundException("User you are trying to follow not found");
        }

        return ur.follow(user, toFollow);
    }

    /**
     * unfollow a user
     * @param user - the user that wants to unfollow another user
     * @param toUnfollow - the user that needs to be unfollowed
     * @return the user
     * @throws InvalidContentException
     * @throws NotFoundException
     */
    public User unfollow(User user, User toUnfollow) throws NotFoundException, InvalidContentException, ActionForbiddenException {
        if(user.getId() <= 0) {
            throw new InvalidContentException("Invalid user ID");
        }

        if(toUnfollow.getId() <= 0) {
            throw new InvalidContentException("Invalid ID for User you are trying to unfollow");
        }

        if(!ur.getById(user.getId()).getFollowing().contains(toUnfollow) || !ur.getById(toUnfollow.getId()).getFollowers().contains(user)) {
            throw new ActionForbiddenException(user.getUsername() + " is not following " + toUnfollow.getUsername());
        }

        if(ur.getById(user.getId()) == null) {
            throw new NotFoundException("User not found");
        }

        if(ur.getById(toUnfollow.getId()) == null) {
            throw new NotFoundException("User you are trying to unfollow not found");
        }

        return ur.unfollow(user, toUnfollow);
    }

    /**
     * Login as a user
     * @param username - the username if the user
     * @param password - the password of the user
     * @return a boolean
     * @throws NoSuchAlgorithmException
     */
    public boolean login(String username, String password) throws NoSuchAlgorithmException {
        return ur.login(username, tempSha256Encryption(password));
    }

    /**
     * Change the role of a user
     * @param user - the user
     * @param role - the role you want to add
     * @return the user
     * @throws InvalidContentException
     * @throws NotFoundException
     */
    public User changeRole(User user, Role role) throws NotFoundException, InvalidContentException {
        if(user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if(role.getId() <= 0) {
            throw new IllegalArgumentException("Invalid role ID");
        }

        if(ur.getById(user.getId()) == null) {
            throw new NotFoundException("User not found");
        }

        if(rr.getById(role.getId()) == null) {
            throw new NotFoundException("Role not found");
        }

        return ur.changeRole(user, role);
    }

    /**
     * Hash a text
     * @param text - the text to hash
     * @return a hashed String
     * @throws NoSuchAlgorithmException
     */
    private String tempSha256Encryption(String text) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(text.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(hash);
    }
}
