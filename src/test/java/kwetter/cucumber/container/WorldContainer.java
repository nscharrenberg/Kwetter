/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.cucumber.container;

import model.Role;
import model.User;
import repository.interfaces.TweetRepository;
import repository.interfaces.UserRepository;
import service.TweetService;
import service.UserService;

import javax.ejb.EJB;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;

public class WorldContainer {

    @Inject
    public UserService userService;

    @Inject
    public TweetService tweetService;

    public Role role;
    public List<User> users;
    public Exception actualException;
}
