/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.steps;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.*;
import cucumber.runtime.junit.Assertions;
import exception.StringToLongException;
import kwetter.container.WorldContainer;
import model.Tweet;
import model.User;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SendTweetStepDef {

    private WorldContainer worldContainer;
    private Tweet tweet;
    private String message;
    private User testUser;

    public SendTweetStepDef(WorldContainer worldContainer) {
        this.worldContainer = worldContainer;
    }

    @When("^testUser wants to send a tweet with the message \"([^\"]*)\"$")
    public void testuser_wants_to_send_a_tweet_with_the_message(String arg1) throws Exception {
        testUser = this.worldContainer.users.get(0);
        message = arg1;

        try {
            tweet = new Tweet();
            tweet.setMessage(message);
            tweet.setAuthor(testUser);
            tweet.setCreatedAt(new Date());
        } catch (StringToLongException e) {
            this.worldContainer.actualException = e;
        }
    }

    @Then("^a tweet should be created$")
    public void a_tweet_should_be_created() {
        assertEquals(message, tweet.getMessage());
        assertEquals(testUser.getUsername(), tweet.getAuthor().getUsername());
    }

    @Then("^an Exception StringToLongException saying \"([^\"]*)\" is thrown\\.$")
    public void an_Exception_StringToLongException_saying_is_thrown(String arg1) {
        assertEquals(arg1, this.worldContainer.actualException.getMessage());
    }
}
