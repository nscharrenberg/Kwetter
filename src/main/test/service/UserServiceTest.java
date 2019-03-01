package service;

import domain.Role;
import domain.User;
import exceptions.CreationFailedException;
import exceptions.InvalidContentException;
import exceptions.NameNotUniqueException;
import exceptions.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import repository.collection.RoleServiceCollImpl;
import repository.collection.UserServiceCollImpl;

import javax.inject.Inject;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Inject
    private UserServiceCollImpl ur;

    @InjectMocks
    private RoleServiceCollImpl rr;

    @InjectMocks
    private UserService userService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void getUserById() {
        int id = 6;
        User user = mock(User.class);
        when(user.getId()).thenReturn(id);

        assertEquals(user.getId(), id);
    }

    @Test
    public void getUserByUsername() {
        String name = "testUser";
        User user = mock(User.class);
        when(user.getUsername()).thenReturn(name);

        assertEquals(user.getUsername(), name);
    }

    @Test
    public void getUserByEmail() {
        String email = "testUSer@mail.com";
        User user = mock(User.class);
        when(user.getEmail()).thenReturn(email);

        assertEquals(user.getEmail(), email);
    }

    @Test
    public void createUser() {
        String name = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "my bio";
        String website = "www.site.com";
        double longitude = 123.123;
        double latitude = 654.23;
        try {
            User user = userService.create(new User(name, email, password, biography, website, longitude, latitude, new Role("member")));
            assertEquals(name, user.getUsername());
            assertEquals(email, user.getEmail());
            assertEquals(biography, user.getBiography());
            assertEquals(website, user.getWebsite());
        } catch (NameNotUniqueException | NoSuchAlgorithmException | CreationFailedException | InvalidContentException e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
    }

    @Test
    public void updateUser() {
        int id = 6;
        String name = "a new name";
        User user = mock(User.class);
        when(user.getId()).thenReturn(id);

        assertEquals(user.getId(), id);
        assertNotEquals(name, user.getUsername());

        user.setUsername(name);

        try {
            User result = userService.update(user);
            assertEquals(id, result.getId());
            assertEquals(name, result.getUsername());
        } catch (NameNotUniqueException | InvalidContentException | NotFoundException e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
    }

    @Test
    public void deleteUser() {
        int id = 6;
        User user = mock(User.class);
        when(user.getId()).thenReturn(id);

        assertEquals(user.getId(), id);

        try {
            boolean result = userService.delete(user);
            assertTrue(result);
        }catch (InvalidContentException | NotFoundException e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
    }

    @Test
    public void followUser() {
        int followerId = 12;
        int followingId = 85;

        User user = mock(User.class);
        User following = mock(User.class);

        when(user.getId()).thenReturn(followerId);
        when(following.getId()).thenReturn(followingId);

        assertEquals(user.getId(), followerId);
        assertEquals(following.getId(), followingId);

        try {
            User result = userService.follow(user, following);
            following = userService.getById(followingId);
            assertTrue(result.getFollowing().contains(following));
            assertTrue(following.getFollowers().contains(result));
        } catch (InvalidContentException |NotFoundException e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
    }

    @Test
    public void unfollowUser() {
        int followerId = 12;
        int followingId = 85;

        User user = mock(User.class);
        User following = mock(User.class);

        when(user.getId()).thenReturn(followerId);
        when(following.getId()).thenReturn(followingId);

        assertEquals(user.getId(), followerId);
        assertEquals(following.getId(), followingId);

        try {
            User result = userService.unfollow(user, following);
            following = userService.getById(followingId);
            assertFalse(result.getFollowing().contains(following));
            assertFalse(following.getFollowers().contains(result));
        } catch (InvalidContentException | NotFoundException e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
    }
}
