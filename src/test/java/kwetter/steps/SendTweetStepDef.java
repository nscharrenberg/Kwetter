/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.steps;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.*;
import cucumber.runtime.junit.Assertions;
import exception.StringToLongException;
import exception.UsernameNotUniqueException;
import kwetter.container.WorldContainer;
import model.Tweet;
import model.User;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

public class SendTweetStepDef {

    private WorldContainer worldContainer;

    public SendTweetStepDef(WorldContainer worldContainer) {
        this.worldContainer = worldContainer;
    }

    @When("^\"([^\"]*)\" wants to send a tweet with the message \"([^\"]*)\"$")
    public void wants_to_send_a_tweet_with_the_message(String arg1, String arg2) throws Exception {
         try {
             worldContainer.tweetService.create(worldContainer.userService.getUserByUsername(arg1), arg2);
         } catch (StringToLongException e) {
             worldContainer.actualException = e;
         }
    }

    @Then("^a tweet should be created and a mention should be added with \"([^\"]*)\"$")
    public void a_tweet_should_be_created_and_a_mention_should_be_added_with(String arg1) throws Exception {
        assertFalse(worldContainer.tweetService.getTweets().get(0).getMentions().isEmpty());
    }

    @Then("^a tweet should be created$")
    public void a_tweet_should_be_created() {
        assertFalse(worldContainer.tweetService.getTweets().isEmpty());
    }

    @Then("^an Exception StringToLongException saying \"([^\"]*)\" is thrown\\.$")
    public void an_Exception_StringToLongException_saying_is_thrown(String arg1) {
        if(this.worldContainer.actualException == null) {
            fail("Expected a StringToLongException");
        }

        assertThatThrownBy(() -> { throw new StringToLongException(arg1); }).isInstanceOf(this.worldContainer.actualException.getClass()).hasMessage(this.worldContainer.actualException.getMessage());
    }

}
