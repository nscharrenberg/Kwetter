package domain;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UserTest {
    private List<User> users;

    @Before
    public void setUp() {
        users = new ArrayList<>();
        Role role = new Role("Member");

        for(int i = 0; i < 10; i++) {
            users.add(new User(i, "testUser" + i, "testUser" + i + "@mail.com", "password", "this is my bio", "www.mysite" + i + ".com", 123123.123123, 123123.123132, role));
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
