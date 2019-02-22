/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.steps;

import com.google.common.collect.Iterables;
import cucumber.api.DataTable;
import cucumber.api.java.en.*;
import kwetter.container.WorldContainer;
import model.*;
import repository.memory.TweetServiceImpl;
import repository.memory.UserServiceImpl;

import java.util.List;

public class UserStepDef {
    private WorldContainer worldContainer;

    public UserStepDef(WorldContainer worldContainer) {
        this.worldContainer = worldContainer;
        this.worldContainer.userService = new UserServiceImpl();
        this.worldContainer.tweetService = new TweetServiceImpl();
    }

    @Given("^the following role:$")
    public void the_following_role(DataTable arg1) throws Exception {
        List<Role> roles = arg1.transpose().asList(Role.class);
        this.worldContainer.role = roles.get(0);
    }

    @Given("^the following users:$")
    public void the_following_users(DataTable arg1) throws Exception {
        List<User> users = arg1.transpose().asList(User.class);
        Iterables.addAll(this.worldContainer.userService.getUsers(), users);
    }
}
