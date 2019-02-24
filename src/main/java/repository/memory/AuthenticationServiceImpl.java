package repository.memory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import model.User;
import org.checkerframework.checker.nullness.qual.Nullable;
import repository.interfaces.AuthenticationRepository;
import repository.interfaces.UserRepository;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

@Stateless
@Default
public class AuthenticationServiceImpl implements AuthenticationRepository {

    @Inject
    private UserRepository ur;

    @Override
    public boolean login(String username, String password) {
        User user = Iterables.tryFind(ur.getUsers(), user1 -> username.equals(user1.getUsername()) && password.equals(user1.getPassword())).orNull();

        return user != null;
    }
}
