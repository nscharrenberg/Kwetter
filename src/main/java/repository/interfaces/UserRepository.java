package repository.interfaces;

import domain.Role;
import domain.User;

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
    boolean login(String username, String password);
    User changeRole(User user, Role role);
}
