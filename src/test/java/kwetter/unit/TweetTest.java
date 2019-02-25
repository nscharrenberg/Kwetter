/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.unit;

import exception.StringToLongException;
import model.Tweet;
import model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.interfaces.TweetRepository;

import java.util.List;

import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class TweetTest {
    @Mock
    private TweetRepository tweetRepository;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void getTweetById() {
        int id = 6;
        String username = "testUser";
        String biography = "This is my biography";
        String website = "www.site.com";
        User user = new User(6, username, "password", biography, 123.123123, 45.423, website, null);

        String message = "This is a tweet";
        Tweet tweet = new Tweet(id, message, user);

        tweetRepository.getTweetById(id);

        assertThat(tweet.getMessage(), is(message));
    }

    public void getTweetByUser() {
        int id = 6;
        String username = "testUser";
        String biography = "This is my biography";
        String website = "www.site.com";
        User user = new User(6, username, "password", biography, 123.123123, 45.423, website, null);

        String message = "This is a tweet";
        Tweet tweet = new Tweet(id, message, user);

        List<Tweet> tweets = tweetRepository.getTweetsByUser(user);

        assertThat(tweets, is(!tweets.isEmpty()));
    }

    @Test
    public void createTweet() {

        int id = 6;
        String username = "testUser";
        String biography = "This is my biography";
        String website = "www.site.com";
        User user = new User(6, username, "password", biography, 123.123123, 45.423, website, null);

        String message = "This is a tweet";
        Tweet tweet = new Tweet(id, message, user);

        try {
            tweetRepository.create(tweet);
        } catch (StringToLongException e) {
            e.printStackTrace();
            fail("Exception not expected during tweet creation");
        }

        try {
            tweet.setMessage("an updated message");
        } catch (StringToLongException e) {
            e.printStackTrace();
            fail("Expection not expected during tweet update");
        }

        tweetRepository.update(tweet);

        //then
        assertThat(tweetRepository.getTweetById(id), is(tweet));
        assertThat(tweetRepository.getTweetById(id).getMessage(), is("an updated message"));
    }

    @Test
    public void updateTweet() {

        int id = 6;
        String username = "testUser";
        String biography = "This is my biography";
        String website = "www.site.com";
        User user = new User(6, username, "password", biography, 123.123123, 45.423, website, null);

        String message = "This is a tweet";
        Tweet tweet = new Tweet(id, message, user);

        try {
            tweetRepository.create(tweet);
        } catch (StringToLongException e) {
            e.printStackTrace();
            fail("Exception not expected during tweet creation");
        }

        //then
        assertThat(tweetRepository.getTweetById(id), is(tweet));


    }
}
