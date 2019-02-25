package repository.memory;

import com.google.common.collect.*;
import exception.StringToLongException;
import exception.UsernameNotUniqueException;
import model.User;
import repository.interfaces.UserRepository;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public User create(User user) throws Exception {
        System.out.println("Create: user - " + user);
        if(user.getBiography().length() > 160) {
            System.out.println("Create: More then 160 chars");
            throw new StringToLongException("Biography can not be more then 160 characters.");
        }
        System.out.println("Create: Less then 160 chars");

        User result = Iterables.tryFind(users, u -> user.getUsername().equals(u.getUsername())).orNull();

        if(result != null) {
            System.out.println("Create: A user is found! - " + result);
            throw new UsernameNotUniqueException("Username must be unique.");
        }
        System.out.println("Create: No Results found");

        if(users.add(user)) {
            System.out.println("Create: User Created");
            return user;
        } else {
            System.out.println("Create: Could not create user");
            throw new Exception("User could not be created");
        }
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
    public Set<User> getFollowersById(int id) {
        return null;
    }

    @Override
    public Set<User> getFollowersByUsername(String username) {
        return null;
    }

    @Override
    public Set<User> getFollowingById(int id) {
        return null;
    }

    @Override
    public Set<User> getFollowingByUsername(String username) {
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
