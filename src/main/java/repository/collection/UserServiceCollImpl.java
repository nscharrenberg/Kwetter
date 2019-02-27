package repository.collection;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import domain.Role;
import domain.User;
import exceptions.NameNotUniqueException;
import org.checkerframework.checker.nullness.qual.Nullable;
import repository.interfaces.UserRepository;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import java.util.ArrayList;
import java.util.List;

@Default
@Stateless
public class UserServiceCollImpl implements UserRepository {

    List<User> users = new ArrayList<>();

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
    public User create(User user) throws NameNotUniqueException, ClassNotFoundException {
        User result = Iterables.tryFind(users, u -> user.getUsername().equals(u.getUsername())).orNull();

        if(result != null) {
            throw new NameNotUniqueException("Username already taken");
        }

        if(users.add(user)) {
            return user;
        } else {
            throw new ClassNotFoundException("Failed to create user");
        }
    }

    @Override
    public User update(User user) throws NameNotUniqueException {
        User result = Iterables.tryFind(users, u -> user.getUsername().equals(u.getUsername()) && user.getId() == u.getId()).orNull();

        if(result != null) {
            throw new NameNotUniqueException("Role name already taken");
        }

        int index = Iterables.indexOf(users, u -> user.getId() == u.getId());

        return users.set(index, user);
    }

    @Override
    public boolean delete(User user) throws ClassNotFoundException {
        User result = Iterables.tryFind(users, u -> user.getId() == u.getId()).orNull();

        if(result == null) {
            throw new ClassNotFoundException("User with id: " + user.getId() + " and username: " + user.getUsername() + " could not be found");
        }

        return users.remove(user);
    }

    @Override
    public User follow(User user, User toFollow) throws ClassNotFoundException, NameNotUniqueException {
        User result = Iterables.tryFind(users, u -> user.getId() == u.getId()).orNull();

        if(result == null) {
            throw new ClassNotFoundException("User with id: " + user.getId() + " and username: " + user.getUsername() + " could not be found");
        }

        user.addFollowing(toFollow);
        update(toFollow);
        return update(user);
    }

    @Override
    public User unfollow(User user, User toUnfollow) throws ClassNotFoundException, NameNotUniqueException {
        User result = Iterables.tryFind(users, u -> user.getId() == u.getId()).orNull();

        if(result == null) {
            throw new ClassNotFoundException("User with id: " + user.getId() + " and username: " + user.getUsername() + " could not be found");
        }

        user.removeFollowing(toUnfollow);
        update(toUnfollow);
        return update(user);
    }

    @Override
    public boolean login(String username, String password) {
        if(username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        User user = Iterables.tryFind(users, new Predicate<User>() {
            @Override
            public boolean apply(@Nullable User user) {
                return username.equals(user.getUsername()) && password.equals(user.getPassword());
            }
        }).orNull();

        return user != null;
    }

    @Override
    public User changeRole(User user, Role role) throws ClassNotFoundException, NameNotUniqueException {
        User result = Iterables.tryFind(users, u -> user.getId() == u.getId()).orNull();

        if(result == null) {
            throw new ClassNotFoundException("User with id: " + user.getId() + " and username: " + user.getUsername() + " could not be found");
        }

        user.setRole(role);
        return update(user);
    }
}
