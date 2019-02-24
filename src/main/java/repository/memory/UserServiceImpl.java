package repository.memory;

import com.google.common.base.*;
import com.google.common.collect.*;
import exception.StringToLongException;
import exception.UsernameNotUniqueException;
import model.User;
import repository.interfaces.UserRepository;

import javax.annotation.Nullable;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Default
public class UserServiceImpl implements UserRepository {

    private List<User> users = new ArrayList<>();

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public User getUserById(int id) {
        return Iterables.tryFind(users,
                user -> id == user.getId()).orNull();
    }

    @Override
    public User getUserByUsername(String username) {
        return Iterables.tryFind(users,
                user -> username.equals(user.getUsername())).orNull();
    }

    @Override
    public void create(User user) throws UsernameNotUniqueException, StringToLongException {
        if(user.getBiography().length() > 160) {
            throw new StringToLongException("Biography can not be more then 160 characters.");
        }

        User result = Iterables.tryFind(users, u -> user.getUsername().equals(u.getUsername())).orNull();

        if(result != null) {
            throw new UsernameNotUniqueException("Username must be unique.");
        }

        users.add(user);
    }

    @Override
    public void follow(User user, User follower) {
        int index = Iterables.indexOf(users, u -> user.getUsername().equals(u.getUsername()));

        users.get(index).addFollowing(follower);
    }

    @Override
    public void unfollow(User user, User follower) {

    }

    @Override
    public List<User> getFollowersById(int id) {
        return null;
    }

    @Override
    public List<User> getFollowersByUsername(String username) {
        return null;
    }

    @Override
    public List<User> getFollowingById(int id) {
        return null;
    }

    @Override
    public List<User> getFollowingByUsername(String username) {
        return null;
    }

    @Override
    public void update(User user) throws UsernameNotUniqueException, StringToLongException {
        if(user.getBiography().length() > 160) {
            throw new StringToLongException("Biography can not be more then 160 characters.");
        }

        User result = Iterables.tryFind(users, u -> user.getUsername().equals(u.getUsername()) && user.getId() != u.getId()).orNull();

        if(result != null) {
            throw new UsernameNotUniqueException("Username must be unique.");
        }

        int index = Iterables.indexOf(users, u -> user.getId() == u.getId());

        users.set(index, user);
    }
}
