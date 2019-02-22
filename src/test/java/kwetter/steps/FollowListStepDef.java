/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.steps;

import com.google.common.collect.*;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import kwetter.container.WorldContainer;
import model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FollowListStepDef {

    private WorldContainer worldContainer;
    private User testUser;
    private User testUser2;
    private Multimap<Integer, String> followerLists;

    public FollowListStepDef(WorldContainer worldContainer) {
        this.worldContainer = worldContainer;
    }

    @Given("^the following followers:$")
    public void the_following_followers(Map<Integer, String> arg1) throws Exception {
        Multimap<Integer, String> mm = ArrayListMultimap.create();
        mm.putAll(Multimaps.forMap(arg1));
        this.followerLists = mm;

        mm.entries().stream().map(e -> {
            int follower = e.getKey();
            String following = e.getValue();

            if(following.contains(",")) {
                List<String> followings = Arrays.asList(following.split(","));
                followings.stream().map(f -> {
                    f = f.trim();

                    worldContainer.userService.getUserById(follower).addFollowing(worldContainer.userService.getUserById(Integer.parseInt(f)));

                    return null;
                });

            } else {
                worldContainer.userService.getUserById(follower).addFollowing(worldContainer.userService.getUserById(Integer.parseInt(following)));
            }

            return null;
        });
    }

    @When("^users with id (\\d+) wants to see who is following him$")
    public void users_at_index_wants_to_see_who_is_following_him(int arg1) throws Exception {
        // Do nothing
    }

    @Then("^user with id (\\d+) should get a list of users that are following him$")
    public void user_at_index_should_get_a_list_of_users_that_are_following_him(int arg1) throws Exception {
        final int[] count = {0};

        followerLists.entries().stream().map(e -> {
            if(e.getValue().contains(Integer.toString(arg1))) {
                count[0]++;
            }

            return null;
        });

        assertEquals(count[0], worldContainer.userService.getUserById(arg1).getFollowers().size());
    }

    @When("^users with id (\\d+) wants to see who he is following$")
    public void users_at_index_wants_to_see_who_he_is_following(int arg1) throws Exception {
        // do nothing
    }

    @Then("^user with id (\\d+) should get a list of users that he is following$")
    public void user_at_index_should_get_a_list_of_users_that_he_is_following(int arg1) throws Exception {
        final int[] count = {0};

        followerLists.entries().stream().map(e -> {
            if(e.getValue().contains(Integer.toString(arg1))) {
                count[0]++;
            }

            return null;
        });

        assertEquals(count[0], worldContainer.userService.getUserById(arg1).getFollowing().size());
    }
}
