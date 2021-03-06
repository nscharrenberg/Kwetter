package com.nscharrenberg.kwetter.domain;

import com.google.common.collect.Iterables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserTest {
    private List<User> users;

    @Before
    public void setUp() {
        users = new ArrayList<>();
        Role role = new Role("Member");

        for(int i = 0; i < 10; i++) {
            User u = new User();
            u.setId(i);
            u.setUsername("testUser" + i);
            u.setPassword("password");
            u.setEmail("testUser" + i + "@mail.com");
            u.setBiography("this is my bio");
            u.setWebsite("www.mysite" + i + ".com");
            u.setLongitude(123123.123123);
            u.setLatitude(123123.123132);
            u.setRole(role);
            users.add(u);
        }
    }

    @After
    public void tearDown() {

    }

    @Test
    public void addFollowingTest() {
        int followerId = 2;
        int followingId = 5;

        User follower = Iterables.tryFind(users, user -> followerId == user.getId()).orNull();
        User following = Iterables.tryFind(users, user -> followerId == user.getId()).orNull();
        addFollowing(follower, following);
    }

    @Test
    public void removeFollowing() {
        int followerId = 2;
        int followingId = 5;

        User follower = Iterables.tryFind(users, user -> followerId == user.getId()).orNull();
        User following = Iterables.tryFind(users, user -> followerId == user.getId()).orNull();
        addFollowing(follower, following);

        assert follower != null;
        assert following != null;
        follower.removeFollowing(following);

        assertFalse(follower.getFollowing().contains(following));
        assertFalse(following.getFollowers().contains(follower));
    }

    private void addFollowing(User follower, User following) {
        if(follower == null && following == null) {
            fail("Follower & Following users expected");
        }

        assert follower != null;
        assert following != null;

        follower.addFollowing(following);

        assertTrue(follower.getFollowing().contains(following));
        assertTrue(following.getFollowers().contains(follower));
    }
}
