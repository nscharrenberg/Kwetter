/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.domain;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import model.Role;
import model.Tweet;
import model.User;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class TweetTest {
    Tweet tweet;
    List<User> users;

    @Before
    public void setUp() throws Exception {
        users = new ArrayList<>();
        Role role = new Role("Member");

        /**
         * Create 10 Users
         */
        for(int i = 0; i < 10; i++) {
            users.add(new User("testUser" + i, "password", "this is my biography" , 123.654,123.1235, "www.site.com" + i, role));
        }

        User user = Iterables.tryFind(users, new Predicate<User>() {
            @Override
            public boolean apply(@Nullable User user) {
                return "testUser1".equals(user.getUsername());
            }
        }).orNull();

        if(user == null) {
            fail("User is NULL");
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    private Tweet addTweet(String message, User user) {
        return new Tweet(message, user);
    }

    @Test
    public void addTweetTest() {
        User user = Iterables.tryFind(users, user1 -> "testUser4".equals(user1.getUsername())).orNull();
        String expectedMessage = "This is my tweet";

        tweet = addTweet(expectedMessage, user);

        assertEquals(expectedMessage, tweet.getMessage());
        assertEquals(user.getUsername(), tweet.getAuthor().getUsername());
    }

    @Test
    public void likeTweet() {
        User author = Iterables.tryFind(users, user1 -> "testUser2".equals(user1.getUsername())).orNull();
        User user = Iterables.tryFind(users, user1 -> "testUser4".equals(user1.getUsername())).orNull();
        String message = "This is my tweet";
        tweet = addTweet(message, author);

        assertEquals(message, tweet.getMessage());
        assertEquals(author.getUsername(), tweet.getAuthor().getUsername());

        if(user == null) {
            fail("User is NULL");
        }

        tweet.addLike(user);

        assertTrue(tweet.getLikes().contains(user));
    }

    @Test
    public void unlikeTweet() {
        User author = Iterables.tryFind(users, user1 -> "testUser2".equals(user1.getUsername())).orNull();
        String message = "This is my tweet";
        tweet = addTweet(message, author);
        assertEquals(message, tweet.getMessage());
        assertEquals(author.getUsername(), tweet.getAuthor().getUsername());

        /**
         * Like Tweet
         */
        User user = Iterables.tryFind(users, user1 -> "testUser4".equals(user1.getUsername())).orNull();

        if(user == null) {
            fail("User is NULL");
        }

        tweet.addLike(user);

        assertTrue(tweet.getLikes().contains(user));

        /**
         * Unlike Tweet
         */

        tweet.removeLike(user);

        assertFalse(tweet.getLikes().contains(user));
    }
}