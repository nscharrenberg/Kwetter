/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.container;

import model.Role;
import model.User;
import repository.memory.TweetServiceImpl;
import repository.memory.UserServiceImpl;
import service.UserService;

import java.util.List;

public class WorldContainer {
    public UserServiceImpl userService;
    public TweetServiceImpl tweetService;
    public Role role;
    public List<User> users;
    public Exception actualException;
}
