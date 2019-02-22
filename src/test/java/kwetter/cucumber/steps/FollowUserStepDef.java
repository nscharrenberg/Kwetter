/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package cucumber.steps;

import cucumber.api.java.en.*;
import cucumber.container.WorldContainer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FollowUserStepDef {
    private WorldContainer worldContainer;

    public FollowUserStepDef(WorldContainer worldContainer) {
        this.worldContainer = worldContainer;
    }

    @Given("^user with id (\\d+) follows user with id (\\d+)$")
    public void user_at_index_follows_user_at_index(int arg1, int arg2) throws Exception {
        worldContainer.userService.getUserById(arg1).addFollowing(worldContainer.userService.getUserById(arg2));
    }

    @When("^user with id (.*) wants to follow user with id (.*)$")
    public void user_at_index_wants_to_follow_user_at_index(int arg1, int arg2) throws Exception {
        worldContainer.userService.getUserById(arg1).addFollowing(worldContainer.userService.getUserById(arg2));
    }

    @Then("^user with id (.*) should be following user with id (.*)$")
    public void user_at_index_should_be_following_user_at_index(int arg1, int arg2) throws Exception {
        assertTrue(worldContainer.userService.getUserById(arg1).getFollowing().contains(worldContainer.userService.getUserById(arg2)));
        assertTrue(worldContainer.userService.getUserById(arg2).getFollowers().contains(worldContainer.userService.getUserById(arg1)));
    }

    @When("^user with id (\\d+) wants to unfollow user with id (\\d+)$")
    public void user_at_index_wants_to_unfollow_user_at_index(int arg1, int arg2) throws Exception {
        worldContainer.userService.getUserById(arg1).removeFollowing(worldContainer.userService.getUserById(arg2));
    }

    @Then("^user with id (\\d+) should be unfollowing user with id (\\d+)$")
    public void user_at_index_should_be_unfollowing_user_at_index(int arg1, int arg2) throws Exception {
        assertFalse(worldContainer.userService.getUserById(arg1).getFollowing().contains(worldContainer.userService.getUserById(arg2)));
        assertFalse(worldContainer.userService.getUserById(arg2).getFollowers().contains(worldContainer.userService.getUserById(arg1)));
    }
}
