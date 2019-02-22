/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.container;

import model.Role;
import model.User;
import repository.interfaces.TweetRepository;
import repository.interfaces.UserRepository;
import repository.memory.TweetServiceImpl;
import repository.memory.UserServiceImpl;
import service.TweetService;
import service.UserService;

import java.util.List;

public class WorldContainer {
    public UserRepository userService;
    public TweetRepository tweetService;
    public Role role;
    public List<User> users;
    public Exception actualException;
}
