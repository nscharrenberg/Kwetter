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
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import repository.collection.RoleServiceCollImpl;
import repository.collection.UserServiceCollImpl;

import javax.inject.Inject;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserServiceCollImpl ur;

    @Mock
    private RoleService roleService;

    @Mock
    private RoleServiceCollImpl rr;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void UserServiceTest() {
        assertNotNull(userService);
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
    public void createUser() throws NoSuchAlgorithmException, CreationFailedException, NotFoundException, InvalidContentException, NameNotUniqueException {
        String name = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "my bio";
        String website = "www.site.com";
        double longitude = 123.123;
        double latitude = 654.23;
        Role role = new Role("member");
        rr.create(role);
        verify(rr, atLeastOnce()).create(role);

        User user = new User(name, email, password, biography, website, longitude, latitude);

        userService.create(user);
        verify(ur, atLeastOnce()).create(user);
    }

    @Test
    public void updateUser() throws InvalidContentException, NameNotUniqueException, NotFoundException {
        int id = 6;
        String name = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "my bio";
        String website = "www.site.com";
        double longitude = 123.123;
        double latitude = 654.23;
        User user = new User(name, email, password, biography, website, longitude, latitude);
        user.setId(id);
        when(ur.getById(id)).thenReturn(user);
        when(ur.getByUsername(name)).thenReturn(user);
        when(ur.getByEmail(email)).thenReturn(user);

        user.setUsername(name);

        userService.update(user);
        verify(ur, atLeastOnce()).update(user);
    }

    @Test(expected = NameNotUniqueException.class)
    public void updateUserDuplicateUsername() throws InvalidContentException, NameNotUniqueException, NotFoundException {
        /**
         * User 1
         */
        int id = 1;
        User user = new User("testUser", "testUser@mail.com", "password123", "my bio", "www.site.com", 123.123, 654.23);
        user.setId(id);
        when(ur.getById(id)).thenReturn(user);
        when(ur.getByUsername("testUser")).thenReturn(user);
        when(ur.getByEmail("testUser@mail.com")).thenReturn(user);

        /**
         * User 2
         */
        int id2 = 2;
        User user2 = new User("testUser2", "testUser2@mail.com", "password1234567", "my bio2", "www.site2.com", 1235.423, 674.234);
        user2.setId(id2);
        when(ur.getById(id2)).thenReturn(user2);
        when(ur.getByUsername("testUser2")).thenReturn(user2);
        when(ur.getByEmail("testUser2@mail.com")).thenReturn(user2);

        user2.setUsername("testUser");

        userService.update(user2);
        verify(ur, atLeastOnce()).update(user2);
    }

    @Test(expected = NameNotUniqueException.class)
    public void updateUserDuplicateEmail() throws InvalidContentException, NameNotUniqueException, NotFoundException {
        /**
         * User 1
         */
        int id = 1;
        User user = new User("testUser", "testUser@mail.com", "password123", "my bio", "www.site.com", 123.123, 654.23);
        user.setId(id);
        when(ur.getById(id)).thenReturn(user);
        when(ur.getByUsername("testUser")).thenReturn(user);
        when(ur.getByEmail("testUser@mail.com")).thenReturn(user);

        /**
         * User 2
         */
        int id2 = 2;
        User user2 = new User("testUser2", "testUser2@mail.com", "password1234567", "my bio2", "www.site2.com", 1235.423, 674.234);
        user2.setId(id2);
        when(ur.getById(id2)).thenReturn(user2);
        when(ur.getByUsername("testUser2")).thenReturn(user2);
        when(ur.getByEmail("testUser2@mail.com")).thenReturn(user2);

        user2.setEmail("testUser@mail.com");

        userService.update(user2);
        verify(ur, atLeastOnce()).update(user2);
    }

    @Test(expected = NotFoundException.class)
    public void getUserIdNotFound() throws InvalidContentException, NotFoundException {
        int id = 6;
        when(ur.getById(id)).thenReturn(null);

        userService.getById(id);
        verify(ur, atLeastOnce()).getById(id);
    }

    @Test(expected = NotFoundException.class)
    public void getUserNameNotFound() throws InvalidContentException, NotFoundException {
        String name = "myUserName";
        when(ur.getByUsername(name)).thenReturn(null);

        userService.getByUsername(name);
        verify(ur, atLeastOnce()).getByUsername(name);
    }

    @Test(expected = NotFoundException.class)
    public void getEmailNotFound() throws InvalidContentException, NotFoundException {
        String email = "my@mail.com";
        when(ur.getByEmail(email)).thenReturn(null);

        userService.getByEmail(email);
        verify(ur, atLeastOnce()).getByEmail(email);
    }

    @Test
    public void deleteUser() throws InvalidContentException, NotFoundException {
        int id = 6;
        String name = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "my bio";
        String website = "www.site.com";
        double longitude = 123.123;
        double latitude = 654.23;
        User user = new User(name, email, password, biography, website, longitude, latitude);
        user.setId(id);
        when(ur.getById(id)).thenReturn(user);
        when(ur.getByUsername(name)).thenReturn(user);
        when(ur.getByEmail(email)).thenReturn(user);

        userService.delete(user);
        verify(ur, atLeastOnce()).delete(user);
    }

    @Test
    public void followUser() throws InvalidContentException, NotFoundException {
        /**
         * User 1
         */
        int id = 1;
        User user = new User("testUser", "testUser@mail.com", "password123", "my bio", "www.site.com", 123.123, 654.23);
        user.setId(id);
        when(ur.getById(id)).thenReturn(user);
        when(ur.getByUsername("testUser")).thenReturn(user);
        when(ur.getByEmail("testUser@mail.com")).thenReturn(user);

        /**
         * User 2
         */
        int id2 = 2;
        User user2 = new User("testUser2", "testUser2@mail.com", "password1234567", "my bio2", "www.site2.com", 1235.423, 674.234);
        user2.setId(id2);
        when(ur.getById(id2)).thenReturn(user2);
        when(ur.getByUsername("testUser2")).thenReturn(user2);
        when(ur.getByEmail("testUser2@mail.com")).thenReturn(user2);

        userService.follow(user, user2);
        verify(ur, atLeastOnce()).follow(user, user2);
    }

    @Test(expected = InvalidContentException.class)
    public void followUserAlreadyFollowing() throws InvalidContentException, NotFoundException {
        /**
         * User 1
         */
        int id = 1;
        User user = new User("testUser", "testUser@mail.com", "password123", "my bio", "www.site.com", 123.123, 654.23);
        user.setId(id);
        when(ur.getById(id)).thenReturn(user);
        when(ur.getByUsername("testUser")).thenReturn(user);
        when(ur.getByEmail("testUser@mail.com")).thenReturn(user);

        /**
         * User 2
         */
        int id2 = 2;
        User user2 = new User("testUser2", "testUser2@mail.com", "password1234567", "my bio2", "www.site2.com", 1235.423, 674.234);
        user2.setId(id2);
        when(ur.getById(id2)).thenReturn(user2);
        when(ur.getByUsername("testUser2")).thenReturn(user2);
        when(ur.getByEmail("testUser2@mail.com")).thenReturn(user2);

        userService.follow(user, user2);
        verify(ur, atLeastOnce()).follow(user, user2);

        userService.follow(user, user2);
        verify(ur, atLeastOnce()).follow(user, user2);
    }

    @Test
    public void unfollowUser() throws InvalidContentException, NotFoundException {
        /**
         * User 1
         */
        int id = 1;
        User user = new User("testUser", "testUser@mail.com", "password123", "my bio", "www.site.com", 123.123, 654.23);
        user.setId(id);
        when(ur.getById(id)).thenReturn(user);
        when(ur.getByUsername("testUser")).thenReturn(user);
        when(ur.getByEmail("testUser@mail.com")).thenReturn(user);

        /**
         * User 2
         */
        int id2 = 2;
        User user2 = new User("testUser2", "testUser2@mail.com", "password1234567", "my bio2", "www.site2.com", 1235.423, 674.234);
        user2.setId(id2);
        when(ur.getById(id2)).thenReturn(user2);
        when(ur.getByUsername("testUser2")).thenReturn(user2);
        when(ur.getByEmail("testUser2@mail.com")).thenReturn(user2);

        userService.follow(user, user2);
        verify(ur, atLeastOnce()).follow(user, user2);

        userService.unfollow(user, user2);
        verify(ur, atLeastOnce()).follow(user, user2);
    }

    @Test(expected = InvalidContentException.class)
    public void unfollowUserYouAreNotFollowing() throws InvalidContentException, NotFoundException {
        /**
         * User 1
         */
        int id = 1;
        User user = new User("testUser", "testUser@mail.com", "password123", "my bio", "www.site.com", 123.123, 654.23);
        user.setId(id);
        when(ur.getById(id)).thenReturn(user);
        when(ur.getByUsername("testUser")).thenReturn(user);
        when(ur.getByEmail("testUser@mail.com")).thenReturn(user);

        /**
         * User 2
         */
        int id2 = 2;
        User user2 = new User("testUser2", "testUser2@mail.com", "password1234567", "my bio2", "www.site2.com", 1235.423, 674.234);
        user2.setId(id2);
        when(ur.getById(id2)).thenReturn(user2);
        when(ur.getByUsername("testUser2")).thenReturn(user2);
        when(ur.getByEmail("testUser2@mail.com")).thenReturn(user2);

        userService.unfollow(user, user2);
        verify(ur, atLeastOnce()).follow(user, user2);
    }
}
