package service;

import domain.Role;
import domain.User;
import exceptions.NameNotUniqueException;
import repository.interfaces.RoleRepository;
import repository.interfaces.UserRepository;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

public class UserService {

    @Inject @Default
    private UserRepository ur;

    @Inject @Default
    private RoleRepository rr;

    public List<User> all() {
        return ur.all();
    }

    public User getById(int id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        User user = ur.getById(id);

        if(user == null) {
            throw new NotFoundException("User not found");
        }

        return user;
    }

    public User getByUsername(String username) {
        if(username.isEmpty()) {
            throw new IllegalArgumentException("username can not be empty");
        }

        User user = ur.getByUsername(username);

        if(user == null) {
            throw new NotFoundException("User not found");
        }

        return user;
    }

    public User getByEmail(String email) {
        if(email.isEmpty()) {
            throw new IllegalArgumentException("username can not be empty");
        }

        User user = ur.getByEmail(email);

        if(user == null) {
            throw new NotFoundException("User not found");
        }

        return user;
    }

    public User create(User user) throws NameNotUniqueException, ClassNotFoundException, NoSuchAlgorithmException {
        if(user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("username can not be empty");
        }

        if(ur.getByUsername(user.getUsername()) != null) {
            throw new NameNotUniqueException("Username already taken");
        }

        user.setPassword(tempSha256Encryption(user.getPassword()));
        return ur.create(user);
    }

    public User update(User user) throws NameNotUniqueException {
        if(user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("username can not be empty");
        }

        if(user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        if(ur.getById(user.getId()) == null) {
            throw new NotFoundException("User not found");
        }

        User result = ur.getByUsername(user.getUsername());
        if(result != null && result.getId() != user.getId()) {
            throw new NameNotUniqueException("Username already taken");
        }

        return ur.update(user);
    }

    public boolean delete(User user) throws ClassNotFoundException {
        if(user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        return ur.delete(user);
    }

    public User follow(User user, User toFollow) throws ClassNotFoundException, NameNotUniqueException {
        if(user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if(toFollow.getId() <= 0) {
            throw new IllegalArgumentException("Invalid ID for User you are trying to follow");
        }

        if(ur.getById(user.getId()) == null) {
            throw new NotFoundException("User not found");
        }

        if(ur.getById(toFollow.getId()) == null) {
            throw new NotFoundException("User you are trying to follow not found");
        }

        return ur.follow(user, toFollow);
    }

    public User unfollow(User user, User toUnfollow) throws ClassNotFoundException, NameNotUniqueException {
        if(user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if(toUnfollow.getId() <= 0) {
            throw new IllegalArgumentException("Invalid ID for User you are trying to unfollow");
        }

        if(ur.getById(user.getId()) == null) {
            throw new NotFoundException("User not found");
        }

        if(ur.getById(toUnfollow.getId()) == null) {
            throw new NotFoundException("User you are trying to unfollow not found");
        }

        return ur.unfollow(user, toUnfollow);
    }

    public boolean login(String username, String password) throws NoSuchAlgorithmException {
        return ur.login(username, tempSha256Encryption(password));
    }

    public User changeRole(User user, Role role) throws ClassNotFoundException, NameNotUniqueException {
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

    private String tempSha256Encryption(String text) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(text.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(hash);
    }
}
