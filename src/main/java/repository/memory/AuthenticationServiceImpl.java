package repository.memory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import model.User;
import org.checkerframework.checker.nullness.qual.Nullable;
import repository.interfaces.AuthenticationRepository;
import repository.interfaces.UserRepository;

import javax.inject.Inject;

public class AuthenticationServiceImpl implements AuthenticationRepository {

    @Inject
    private UserRepository ur;

    @Override
    public boolean login(String username, String password) {
        User user = Iterables.tryFind(ur.getUsers(), new Predicate<User>() {
            @Override
            public boolean apply(@Nullable User user) {
                return username.equals(user.getUsername()) && password.equals(user.getPassword());
            }
        }).orNull();

        return user != null;
    }
}
