package domain;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TweetTest {
    private List<User> users;
    private Tweet tweet;
    private String message;

    @Before
    public void setUp() {
        users = new ArrayList<>();
        Role role = new Role("Member");

        for(int i = 0; i < 10; i++) {
            users.add(new User(i, "testUser" + i, "testUser" + i + "@mail.com", "password", "this is my bio", "www.mysite" + i + ".com", 123123.123123, 123123.123132, role));
        }

        int userId = 4;

        User user = Iterables.tryFind(users, user1 -> userId == user1.getId()).orNull();

        if(user == null) {
            fail("User is NULL");
        }

        message = "This is a new tweet";
        tweet = new Tweet(1,message,user, new Date());
    }

    @After
    public void tearDown() {

    }

    @Test
    public void likeTweetTest() {
        User user = Iterables.tryFind(users, user1 -> "testUser4".equals(user1.getUsername())).orNull();

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
