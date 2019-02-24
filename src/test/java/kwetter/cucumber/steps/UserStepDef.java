/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.cucumber.steps;

import com.google.common.collect.Iterables;
import cucumber.api.DataTable;
import cucumber.api.java.en.*;
import kwetter.cucumber.container.WorldContainer;
import model.*;
import service.TweetService;
import service.UserService;

import java.util.List;

public class UserStepDef {
    private WorldContainer worldContainer;

    public UserStepDef(WorldContainer worldContainer) {
        this.worldContainer = worldContainer;
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
