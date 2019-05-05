package com.nscharrenberg.kwetter.repository.collection;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.nscharrenberg.kwetter.domain.Role;
import com.nscharrenberg.kwetter.domain.User;
import com.nscharrenberg.kwetter.exceptions.NameNotUniqueException;
import org.checkerframework.checker.nullness.qual.Nullable;
import com.nscharrenberg.kwetter.repository.interfaces.UserRepository;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import java.util.ArrayList;
import java.util.List;

@Default
@Stateful
public class UserServiceCollImpl implements UserRepository {

    private List<User> users = new ArrayList<>();

    public UserServiceCollImpl() {
    }

    @Override
    public List<User> all() {
        return users;
    }

    @Override
    public User getById(int id) {
        return Iterables.tryFind(users, user -> id == user.getId()).orNull();
    }

    @Override
    public User getByUsername(String username) {
        return Iterables.tryFind(users, user -> username.equals(user.getUsername())).orNull();
    }

    @Override
    public User getByEmail(String email) {
        return Iterables.tryFind(users, user -> email.equals(user.getEmail())).orNull();
    }

    @Override
    public User create(User user) {
        user.setId(all().size() + 1);
        if(users.add(user)) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public User update(User user) {
        int index = Iterables.indexOf(users, u -> user.getId() == u.getId());
        return users.set(index, user);
    }

    @Override
    public boolean delete(User user) {
        return users.remove(user);
    }

    @Override
    public User follow(User user, User toFollow) {
        user.addFollowing(toFollow);
        update(toFollow);
        return update(user);
    }

    @Override
    public User unfollow(User user, User toUnfollow) {
        user.removeFollowing(toUnfollow);
        update(toUnfollow);
        return update(user);
    }

    @Override
    public User changeRole(User user, Role role) {
        user.setRole(role);
        return update(user);
    }

    @Override
    public User login(String username, String encryptedPassword) {
        return null;
    }
}
