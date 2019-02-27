package repository.interfaces;

import domain.Role;
import domain.User;
import exceptions.NameNotUniqueException;

import java.util.List;

public interface UserRepository {
    List<User> all();
    User getById(int id);
    User getByUsername(String username);
    User getByEmail(String email);
    User create(User user) throws NameNotUniqueException, ClassNotFoundException;
    User update(User user) throws NameNotUniqueException;
    boolean delete(User user) throws ClassNotFoundException;
    User follow(User user, User toFollow) throws ClassNotFoundException, NameNotUniqueException;
    User unfollow(User user, User toUnfollow) throws ClassNotFoundException, NameNotUniqueException;
    boolean login(String username, String password);
    User changeRole(User user, Role role) throws ClassNotFoundException, NameNotUniqueException;
}
