/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.steps;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.*;
import kwetter.container.WorldContainer;
import model.Tweet;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LikeTweetStepDef {
    private WorldContainer worldContainer;
    private Tweet tweet;

    public LikeTweetStepDef(WorldContainer worldContainer) {
        this.worldContainer = worldContainer;
    }

    @Given("^the following tweet:$")
    public void the_following_tweet(DataTable arg1) throws Exception {
        List<Tweet> tweets = arg1.transpose().asList(Tweet.class);
        tweet = tweets.get(0);
        tweet.setAuthor(this.worldContainer.users.get(1));
    }

    @When("^testUser likes a Tweet$")
    public void testuser_likes_a_Tweet() throws Exception {
        tweet.addLike(this.worldContainer.users.get(0));
    }

    @Then("^the Tweet should have one like by testUser$")
    public void the_Tweet_should_have_one_like_by_testUser() throws Exception {
        assertFalse(tweet.getLikes().isEmpty());
    }

    @When("^testUser unlikes a Tweet$")
    public void testuser_unlikes_a_Tweet() throws Exception {
        tweet.removeLike(this.worldContainer.users.get(0));
    }

    @Then("^the Tweet should not have a like by testUser anymore$")
    public void the_Tweet_should_not_have_a_like_by_testUser_anymore() throws Exception {
        assertTrue(tweet.getLikes().isEmpty());
    }
}
