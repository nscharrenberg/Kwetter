package com.nscharrenberg.kwetter.repository.interfaces;

import com.nscharrenberg.kwetter.domain.Role;
import com.nscharrenberg.kwetter.domain.User;

import java.util.List;

public interface UserRepository {
    List<User> all();
    User getById(int id);
    User getByUsername(String username);
    User getByEmail(String email);
    User create(User user);
    User update(User user);
    boolean delete(User user);
    User follow(User user, User toFollow);
    User unfollow(User user, User toUnfollow);
    User changeRole(User user, Role role);
    User login(String username, String encryptedPassword);
}
