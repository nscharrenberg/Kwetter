package repository.memory;

import com.google.common.base.*;
import com.google.common.collect.*;
import model.User;
import repository.interfaces.UserRepository;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserRepository {
    private List<User> users;

    public UserServiceImpl() {
        this.users = new ArrayList<>();
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public User getUserById(int id) {
        return Iterables.tryFind(users,
                new Predicate<User>() {
                    @Override
                    public boolean apply(@Nullable User user) {
                        return Integer.toString(id).equals(Integer.toString(user.getId()));
                    }
                }).orNull();
    }

    @Override
    public User getUserByUsername(String username) {
        return Iterables.tryFind(users,
                new Predicate<User>() {
                    @Override
                    public boolean apply(@Nullable User user) {
                        return username.equals(user.getUsername());
                    }
                }).orNull();
    }

    @Override
    public void create(User user) {
        users.add(user);
    }

    @Override
    public void follow(User user, User follower) {
        int index = Iterables.indexOf(users, new Predicate<User>() {
            @Override
            public boolean apply(@Nullable User u) {
                return user.getUsername().equals(u.getUsername());
            }
        });

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
    public void update(User user) {

    }
}
