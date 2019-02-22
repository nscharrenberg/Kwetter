/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package domain;

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
//            users.add(new User(i, "testUser" + i, "password", "this is my bio", "www.mysite" + i + ".com", 123123.123123, 123123.123132, role));
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

//        tweet = new Tweet(1, "this is a tweet", user, new Date());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void likeTweet() {
        User user = Iterables.tryFind(users, new Predicate<User>() {
            @Override
            public boolean apply(@Nullable User user) {
                return "testUser4".equals(user.getUsername());
            }
        }).orNull();

        if(user == null) {
            fail("User is NULL");
        }

        tweet.addLike(user);

        assertTrue(tweet.getLikes().contains(user));
    }

    @Test
    public void unlikeTweet() {
        /**
         * Like Tweet
         */
        User user = Iterables.tryFind(users, new Predicate<User>() {
            @Override
            public boolean apply(@Nullable User user) {
                return "testUser4".equals(user.getUsername());
            }
        }).orNull();

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