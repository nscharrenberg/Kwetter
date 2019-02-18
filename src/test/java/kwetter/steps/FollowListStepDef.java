/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.steps;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.*;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import kwetter.container.WorldContainer;
import model.Role;
import model.User;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FollowListStepDef {

    private WorldContainer worldContainer;
    private User testUser;
    private User testUser2;
    private Map<Integer, String> followerLists;

    public FollowListStepDef(WorldContainer worldContainer) {
        this.worldContainer = worldContainer;
    }

    @Given("^the following followers:$")
    public void the_following_followers(Map<Integer, String> arg1) throws Exception {
        this.followerLists = arg1;

        for (Map.Entry<Integer, String> entry : arg1.entrySet()) {
            int follower = entry.getKey();
            String following = entry.getValue();

            if(following.contains(",")) {
                String[] followings = following.split(",");

                for (String f : followings) {
                    f = f.trim();
                    worldContainer.users.get(follower).addFollowing(worldContainer.users.get(Integer.parseInt(f)));
                    worldContainer.users.get(Integer.parseInt(f)).addFollower(worldContainer.users.get(follower));
                }
            } else {
                worldContainer.users.get(follower).addFollowing(worldContainer.users.get(Integer.parseInt(following)));
                worldContainer.users.get(Integer.parseInt(following)).addFollower(worldContainer.users.get(follower));
            }
        }
    }

    @When("^users at index (\\d+) wants to see who is following him$")
    public void users_at_index_wants_to_see_who_is_following_him(int arg1) throws Exception {
        // Do nothing
    }

    @Then("^user at index (\\d+) should get a list of users that are following him$")
    public void user_at_index_should_get_a_list_of_users_that_are_following_him(int arg1) throws Exception {
        int count = 0;
        for(Map.Entry<Integer, String> entry : followerLists.entrySet()) {
            if(entry.getValue().contains(Integer.toString(arg1))) {
                count++;
            }
        }

        assertEquals(count, worldContainer.users.get(arg1).getFollowers().size());
    }

    @When("^users at index (\\d+) wants to see who he is following$")
    public void users_at_index_wants_to_see_who_he_is_following(int arg1) throws Exception {
        // do nothing
    }

    @Then("^user at index (\\d+) should get a list of users that he is following$")
    public void user_at_index_should_get_a_list_of_users_that_he_is_following(int arg1) throws Exception {
        String[] followings = followerLists.get(arg1).split(",");

        assertEquals(followings.length, worldContainer.users.get(arg1).getFollowing().size());
    }
}
