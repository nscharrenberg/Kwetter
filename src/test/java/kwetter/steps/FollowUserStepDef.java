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

public class FollowUserStepDef {
    private WorldContainer worldContainer;
    private User testUser;
    private User testUser2;

    public FollowUserStepDef(WorldContainer worldContainer) {
        this.worldContainer = worldContainer;
    }

    @When("^user at index (.*) wants to follow user at index (.*)$")
    public void user_at_index_wants_to_follow_user_at_index(int arg1, int arg2) throws Exception {
        User user = this.worldContainer.users.get(arg1);
        user.addFollower(this.worldContainer.users.get(arg2));
//        this.worldContainer.users.get(arg1).addFollower(testUser);
//        this.worldContainer.users.get(arg2).addFollowing(testUser2);
    }

    @Then("^user at index (.*) should be following user at index (.*)$")
    public void user_at_index_should_be_following_user_at_index(int arg1, int arg2) throws Exception {
        int test = 2;
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
