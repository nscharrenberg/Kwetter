/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package domain;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import model.Role;
import model.User;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserTest {
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
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addFollowing() {
        User follower = Iterables.tryFind(users, new Predicate<User>() {
            @Override
            public boolean apply(@Nullable User user) {
                return Integer.toString(2).equals(Integer.toString(user.getId()));
            }
        }).orNull();

        User following = Iterables.tryFind(users, new Predicate<User>() {
            @Override
            public boolean apply(@Nullable User user) {
                return Integer.toString(5).equals(Integer.toString(user.getId()));
            }
        }).orNull();

        if(follower == null && following == null) {
            fail("Follower & Following users expected");
        }

        assert follower != null;
        assert following != null;

        follower.addFollowing(following);

        assertTrue(follower.getFollowing().contains(following));
        assertTrue(following.getFollowers().contains(follower));
    }

    @Test
    public void removeFollowing() {
        /**
         * Add Following
         */
        User follower = Iterables.tryFind(users, new Predicate<User>() {
            @Override
            public boolean apply(@Nullable User user) {
                return Integer.toString(2).equals(Integer.toString(user.getId()));
            }
        }).orNull();

        User following = Iterables.tryFind(users, new Predicate<User>() {
            @Override
            public boolean apply(@Nullable User user) {
                return Integer.toString(5).equals(Integer.toString(user.getId()));
            }
        }).orNull();

        assert follower != null;
        assert following != null;

        follower.addFollowing(following);

        assertTrue(follower.getFollowing().contains(following));
        assertTrue(following.getFollowers().contains(follower));

        /**
         * Remove following
         */

        follower.removeFollowing(following);

        assertFalse(follower.getFollowing().contains(following));
        assertFalse(following.getFollowers().contains(follower));
    }
}