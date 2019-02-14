/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.steps;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.*;
import kwetter.container.WorldContainer;
import model.User;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FollowUserStepDef {
    private WorldContainer worldContainer;
    private User testUser;
    private User testUser2;

    public FollowUserStepDef(WorldContainer worldContainer) {
        this.worldContainer = worldContainer;
    }

    @Given("^user at index (\\d+) follows user at index (\\d+)$")
    public void user_at_index_follows_user_at_index(int arg1, int arg2) throws Exception {
        testUser = worldContainer.users.get(arg1);
        testUser2 = worldContainer.users.get(arg2);

        testUser2.addFollower(testUser);
        testUser.addFollowing(testUser2);
    }

    @When("^user at index (.*) wants to follow user at index (.*)$")
    public void user_at_index_wants_to_follow_user_at_index(int arg1, int arg2) throws Exception {
        testUser = worldContainer.users.get(arg1);
        testUser2 = worldContainer.users.get(arg2);

        testUser2.addFollower(testUser);
        testUser.addFollowing(testUser2);
    }

    @Then("^user at index (.*) should be following user at index (.*)$")
    public void user_at_index_should_be_following_user_at_index(int arg1, int arg2) throws Exception {
        assertEquals(testUser.getId(), worldContainer.users.get(arg1).getId());
        assertEquals(testUser2.getId(), worldContainer.users.get(arg2).getId());
        assertTrue(testUser2.getFollowers().contains(testUser));
        assertTrue(testUser.getFollowing().contains(testUser2));
    }

    @When("^user at index (\\d+) wants to unfollow user at index (\\d+)$")
    public void user_at_index_wants_to_unfollow_user_at_index(int arg1, int arg2) throws Exception {
        testUser2.removeFollower(testUser);
        testUser.removeFollowing(testUser2);
    }

    @Then("^user at index (\\d+) should be unfollowing user at index (\\d+)$")
    public void user_at_index_should_be_unfollowing_user_at_index(int arg1, int arg2) throws Exception {
        assertEquals(testUser.getId(), worldContainer.users.get(arg1).getId());
        assertEquals(testUser2.getId(), worldContainer.users.get(arg2).getId());
        assertFalse(testUser2.getFollowers().contains(testUser));
        assertFalse(testUser.getFollowing().contains(testUser2));
    }
}
