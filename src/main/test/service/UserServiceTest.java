package service;

import domain.User;
import exceptions.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import repository.interfaces.UserRepository;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository ur;

    @Mock
    private RoleService roleService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void UserServiceTest() {
        assertNotNull(userService);
    }

    @Test
    public void getUserById() throws InvalidContentException, NotFoundException {
        int id = 6;
        User user = mock(User.class);
        user.setId(id);
        when(ur.getById(id)).thenReturn(user);

        userService.getById(id);
        verify(ur, atLeastOnce()).getById(id);
    }

    @Test
    public void getUserByUsername() throws InvalidContentException, NotFoundException {
        String name = "testUser";
        User user = mock(User.class);
        user.setUsername(name);
        when(ur.getByUsername(name)).thenReturn(user);

        userService.getByUsername(name);
        verify(ur, atLeastOnce()).getByUsername(name);
    }

    @Test
    public void getUserByEmail() throws InvalidContentException, NotFoundException {
        String email = "testUSer@mail.com";
        User user = mock(User.class);
        user.setEmail(email);
        when(ur.getByEmail(email)).thenReturn(user);

        userService.getByEmail(email);
        verify(ur, atLeastOnce()).getByEmail(email);
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

        User user = new User(name, email, password, biography, website, longitude, latitude);
        when(ur.create(user)).thenReturn(user);

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
        user.setUsername(name);

        when(ur.getById(id)).thenReturn(user);
        when(ur.update(user)).thenReturn(user);

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

        /**
         * User 2
         */
        int id2 = 2;
        User user2 = new User("testUser2", "testUser2@mail.com", "password1234567", "my bio2", "www.site2.com", 1235.423, 674.234);
        user2.setId(id2);
        user2.setUsername("testUser");

        when(ur.getById(id2)).thenReturn(user2);
        when(ur.getByUsername("testUser")).thenReturn(user);

        userService.update(user2);
        verify(ur, never()).update(user2);
    }

    @Test(expected = NameNotUniqueException.class)
    public void updateUserDuplicateEmail() throws InvalidContentException, NameNotUniqueException, NotFoundException {
        /**
         * User 1
         */
        int id = 1;
        User user = new User("testUser", "testUser@mail.com", "password123", "my bio", "www.site.com", 123.123, 654.23);
        user.setId(id);

        /**
         * User 2
         */
        int id2 = 2;
        User user2 = new User("testUser2", "testUser2@mail.com", "password1234567", "my bio2", "www.site2.com", 1235.423, 674.234);
        user2.setId(id2);
        user2.setEmail("testUser@mail.com");

        when(ur.getById(id2)).thenReturn(user2);
        when(ur.getByEmail("testUser@mail.com")).thenReturn(user);

        userService.update(user2);
        verify(ur, never()).update(user2);
    }

    @Test(expected = NotFoundException.class)
    public void getUserIdNotFound() throws InvalidContentException, NotFoundException {
        int id = 6;
        when(ur.getById(id)).thenReturn(null);

        userService.getById(id);
        verify(ur, never()).getById(id);
    }

    @Test(expected = NotFoundException.class)
    public void getUserNameNotFound() throws InvalidContentException, NotFoundException {
        String name = "myUserName";
        when(ur.getByUsername(name)).thenReturn(null);

        userService.getByUsername(name);
        verify(ur, never()).getByUsername(name);
    }

    @Test(expected = NotFoundException.class)
    public void getEmailNotFound() throws InvalidContentException, NotFoundException {
        String email = "my@mail.com";
        when(ur.getByEmail(email)).thenReturn(null);

        userService.getByEmail(email);
        verify(ur, never()).getByEmail(email);
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
        when(ur.delete(user)).thenReturn(true);

        userService.delete(user);
        verify(ur, atLeastOnce()).delete(user);
    }

    @Test
    public void followUser() throws InvalidContentException, NotFoundException, ActionForbiddenException {
        /**
         * User 1
         */
        int id = 1;
        User user = new User("testUser", "testUser@mail.com", "password123", "my bio", "www.site.com", 123.123, 654.23);
        user.setId(id);

        /**
         * User 2
         */
        int id2 = 2;
        User user2 = new User("testUser2", "testUser2@mail.com", "password1234567", "my bio2", "www.site2.com", 1235.423, 674.234);
        user2.setId(id2);

        when(ur.getById(id)).thenReturn(user);
        when(ur.getById(id2)).thenReturn(user2);
        when(ur.follow(user, user2)).thenReturn(user2);

        userService.follow(user, user2);
        verify(ur, atLeastOnce()).follow(user, user2);
    }

    @Test(expected = ActionForbiddenException.class)
    public void followUserAlreadyFollowing() throws InvalidContentException, NotFoundException, ActionForbiddenException {
        /**
         * User 1
         */
        int id = 1;
        User user = new User("testUser", "testUser@mail.com", "password123", "my bio", "www.site.com", 123.123, 654.23);
        user.setId(id);

        /**
         * User 2
         */
        int id2 = 2;
        User user2 = new User("testUser2", "testUser2@mail.com", "password1234567", "my bio2", "www.site2.com", 1235.423, 674.234);
        user2.setId(id2);

        user.addFollowing(user2);

        when(ur.getById(id)).thenReturn(user);

        userService.follow(user, user2);
        verify(ur, never()).follow(user, user2);
    }

    @Test
    public void unfollowUser() throws InvalidContentException, NotFoundException, ActionForbiddenException {
        /**
         * User 1
         */
        int id = 1;
        User user = new User("testUser", "testUser@mail.com", "password123", "my bio", "www.site.com", 123.123, 654.23);
        user.setId(id);

        /**
         * User 2
         */
        int id2 = 2;
        User user2 = new User("testUser2", "testUser2@mail.com", "password1234567", "my bio2", "www.site2.com", 1235.423, 674.234);
        user2.setId(id2);
        user.addFollowing(user2);

        when(ur.getById(id)).thenReturn(user);
        when(ur.getById(id2)).thenReturn(user2);
        when(ur.unfollow(user, user2)).thenReturn(user2);

        userService.unfollow(user, user2);
        verify(ur, atLeastOnce()).unfollow(user, user2);
    }

    @Test(expected = ActionForbiddenException.class)
    public void unfollowUserYouAreNotFollowing() throws InvalidContentException, NotFoundException, ActionForbiddenException {
        /**
         * User 1
         */
        int id = 1;
        User user = new User("testUser", "testUser@mail.com", "password123", "my bio", "www.site.com", 123.123, 654.23);
        user.setId(id);

        /**
         * User 2
         */
        int id2 = 2;
        User user2 = new User("testUser2", "testUser2@mail.com", "password1234567", "my bio2", "www.site2.com", 1235.423, 674.234);
        user2.setId(id2);

        when(ur.getById(id)).thenReturn(user);

        userService.unfollow(user, user2);
        verify(ur, never()).follow(user, user2);
    }
}
