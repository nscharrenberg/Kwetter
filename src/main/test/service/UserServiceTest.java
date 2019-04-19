package service;

import authentication.PasswordAuthentication;
import domain.Role;
import domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import repository.interfaces.UserRepository;
import responses.HttpStatusCodes;
import responses.ObjectResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository ur;

    @Mock
    private RoleService rr;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void UserServiceTest() {
        assertNotNull(userService);
    }

    private List<User> userList() {
        // Arrange
        List<User> list = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
            User u = new User();
            u.setId(i + 1);
            u.setUsername("user" + i);
            u.setEmail("user" + i + "@mail.com");
            u.setPassword("password" + i);
            u.setLongitude(345.123);
            u.setLatitude(653.234);
            u.setWebsite("www.user" + i + ".com");
            u.setBiography("This is the biography of user" + i);
            list.add(u);
        }

        return list;
    }

    /*
     * Get All Users Test
     */
    @Test
    public void all_StatusCodeOk() {
        // Arrange
        List<User> list = userList();
        when(ur.all()).thenReturn(list);

        // Act
        ObjectResponse<List<User>> response = userService.all();

        // Assert
        verify(ur, atLeastOnce()).all();
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(list, response.getObject());
        assertEquals(list.size(), response.getObject().size());
    }

    /*
     * Get Permission By Id Tests
     */
    @Test
    public void getById_ExistingId_StatusCodeOk() {
        // Arange
        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        when(ur.getById(id)).thenReturn(user);

        // Act
        ObjectResponse<User> response = userService.getById(id);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(user, response.getObject());
    }

    @Test
    public void getById_IdNull_StatusCodeNotAcceptable() {
        // Arange
        int id = 0;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        // Act
        ObjectResponse<User> response = userService.getById(id);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void getById_IdDoesNotExist_StatusCodeNotFound() {
        // Arange
        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        when(ur.getById(id)).thenReturn(null);

        // Act
        ObjectResponse<User> response = userService.getById(id);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }


    /*
     * Get Permission By Username Tests
     */
    @Test
    public void getByUsername_ExistingUsername_StatusCodeOk() {
        // Arange
        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        when(ur.getByUsername(username)).thenReturn(user);

        // Act
        ObjectResponse<User> response = userService.getByUsername(username);

        // Assert
        verify(ur, atLeastOnce()).getByUsername(username);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(user, response.getObject());
    }

    @Test
    public void getByUsername_UsernameNull_StatusCodeNotAcceptable() {
        // Arange
        int id = 15;
        String username = "";
        String email = "testUser@mail.com";
        String password = "password123";
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        // Act
        ObjectResponse<User> response = userService.getByUsername(username);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void getByUsername_UsernameDoesNotExist_StatusCodeNotFound() {
        // Arange
        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        when(ur.getByUsername(username)).thenReturn(null);

        // Act
        ObjectResponse<User> response = userService.getByUsername(username);

        // Assert
        verify(ur, atLeastOnce()).getByUsername(username);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * Get Permission By Username Tests
     */
    @Test
    public void getByEmail_ExistingEmail_StatusCodeOk() {
        // Arange
        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        when(ur.getByEmail(email)).thenReturn(user);

        // Act
        ObjectResponse<User> response = userService.getByEmail(email);

        // Assert
        verify(ur, atLeastOnce()).getByEmail(email);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(user, response.getObject());
    }

    @Test
    public void getByEmail_EmailNull_StatusCodeNotAcceptable() {
        // Arange
        int id = 866;
        String username = "testUser";
        String email = "";
        String password = "password123";
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        // Act
        ObjectResponse<User> response = userService.getByEmail(email);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void getByEmail_EmailDoesNotExist_StatusCodeNotFound() {
        // Arange
        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        when(ur.getByEmail(email)).thenReturn(null);

        // Act
        ObjectResponse<User> response = userService.getByEmail(email);

        // Assert
        verify(ur, atLeastOnce()).getByEmail(email);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * Create User Tests
     */
    @Test
    public void create_newWithProperItems_StatusCodeOk() {
        // Arange
        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        String roleName = "member";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        Role role = new Role();
        role.setId(id);
        role.setName(roleName);

        when(ur.getByUsername(username)).thenReturn(null);
        when(ur.getByEmail(email)).thenReturn(null);
        when(rr.getByName(roleName)).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "Role with name: " + role.getName() + " found", role));
        when(ur.create(user)).thenReturn(user);

        // Act
        ObjectResponse<User> response = userService.create(user);

        // Assert
        verify(ur, atLeastOnce()).getByUsername(username);
        verify(ur, atLeastOnce()).getByEmail(email);
        verify(rr, atLeastOnce()).getByName(roleName);
        verify(ur, atLeastOnce()).create(user);
        assertEquals(HttpStatusCodes.CREATED, response.getCode());
        assertEquals(user, response.getObject());
    }

    @Test
    public void create_EmptyUsername_StatusCodeNotAcceptable() {
        // Arange
        int id = 6;
        String username = "";
        String email = "testUser@mail.com";
        String password = "password123";

        String roleName = "member";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        Role role = new Role();
        role.setId(id);
        role.setName(roleName);

        // Act
        ObjectResponse<User> response = userService.create(user);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void create_EmptyEmail_StatusCodeNotAcceptable() {
        // Arange
        int id = 6;
        String username = "testUser";
        String email = "";
        String password = "password123";

        String roleName = "member";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        Role role = new Role();
        role.setId(id);
        role.setName(roleName);

        // Act
        ObjectResponse<User> response = userService.create(user);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void create_newWithExistingUsername_StatusCodeConflict() {
        // Arange
        int existingId = 6;
        String existingEmail = "testUser@mail.com";
        String existingPassword = "password123";

        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";

        String roleName = "member";

        User existing = new User();
        existing.setId(existingId);
        existing.setUsername(username);
        existing.setEmail(existingEmail);
        existing.setPassword(existingPassword);

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        Role role = new Role();
        role.setId(id);
        role.setName(roleName);

        when(ur.getByUsername(username)).thenReturn(existing);

        // Act
        ObjectResponse<User> response = userService.create(user);

        // Assert
        verify(ur, atLeastOnce()).getByUsername(username);
        assertEquals(HttpStatusCodes.CONFLICT, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void create_newWithExistingEmail_StatusCodeConflict() {
        // Arange
        int existingId = 6;
        String existingUsername = "testUSer2";
        String existingPassword = "password123";

        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";

        String roleName = "member";

        User existing = new User();
        existing.setId(existingId);
        existing.setUsername(existingUsername);
        existing.setEmail(email);
        existing.setPassword(existingPassword);

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        Role role = new Role();
        role.setId(id);
        role.setName(roleName);

        when(ur.getByUsername(username)).thenReturn(null);
        when(ur.getByEmail(email)).thenReturn(existing);

        // Act
        ObjectResponse<User> response = userService.create(user);

        // Assert
        verify(ur, atLeastOnce()).getByUsername(username);
        verify(ur, atLeastOnce()).getByEmail(email);
        assertEquals(HttpStatusCodes.CONFLICT, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void create_newWithBiographyToLong_StatusCodeNotAcceptable() {
        // Arange
        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "DiMg5G7a0WtzAMM2lZGtRDUxGPEzSz8IiTp9kdEFIjQv4Ry0fWIj6g954aJH3nR1tA9vphw3BPjtBtJrBevfSdItG1PNQp2ZSkXJ0ANeLPS03l0aw4X70I2adMZQ5rhKo7FYqszRiUYUsoLU5rt4viJ2UUVak08RHsqa3WErgUaqojWdx7vb2L4XDvCUDhzR4b56";

        String roleName = "member";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        Role role = new Role();
        role.setId(id);
        role.setName(roleName);

        when(ur.getByUsername(username)).thenReturn(null);
        when(ur.getByEmail(email)).thenReturn(null);

        // Act
        ObjectResponse<User> response = userService.create(user);

        // Assert
        verify(ur, atLeastOnce()).getByUsername(username);
        verify(ur, atLeastOnce()).getByEmail(email);
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void create_newWithRoleDoesNotExist_StatusCodeConflict() {
        // Arange
        int existingId = 6;
        String existingUsername = "testUSer2";
        String existingPassword = "password123";

        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";

        String roleName = "member";

        User existing = new User();
        existing.setId(existingId);
        existing.setUsername(existingUsername);
        existing.setEmail(email);
        existing.setPassword(existingPassword);

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        when(ur.getByUsername(username)).thenReturn(null);
        when(ur.getByEmail(email)).thenReturn(null);
        when(rr.getByName(roleName)).thenReturn(new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "Role not found"));

        // Act
        ObjectResponse<User> response = userService.create(user);

        // Assert
        verify(ur, atLeastOnce()).getByUsername(username);
        verify(ur, atLeastOnce()).getByEmail(email);
        verify(rr, atLeastOnce()).getByName(roleName);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * Update User Tests
     */
    @Test
    public void update_newWithProperItems_StatusCodeOk() {
        // Arange
        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        String roleName = "member";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        Role role = new Role();
        role.setId(id);
        role.setName(roleName);

        when(ur.getById(id)).thenReturn(user);
        when(ur.getByUsername(username)).thenReturn(null);
        when(ur.getByEmail(email)).thenReturn(null);
        when(ur.update(user)).thenReturn(user);

        // Act
        ObjectResponse<User> response = userService.update(user);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        verify(ur, atLeastOnce()).getByUsername(username);
        verify(ur, atLeastOnce()).getByEmail(email);
        verify(ur, atLeastOnce()).update(user);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(user, response.getObject());
    }

    @Test
    public void update_EmptyUsername_StatusCodeNotAcceptable() {
        // Arange
        int id = 6;
        String username = "";
        String email = "testUser@mail.com";
        String password = "password123";

        String roleName = "member";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        Role role = new Role();
        role.setId(id);
        role.setName(roleName);

        // Act
        ObjectResponse<User> response = userService.update(user);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void update_EmptyEmail_StatusCodeNotAcceptable() {
        // Arange
        int id = 6;
        String username = "testUser";
        String email = "";
        String password = "password123";

        String roleName = "member";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        Role role = new Role();
        role.setId(id);
        role.setName(roleName);

        // Act
        ObjectResponse<User> response = userService.update(user);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void update_newWithExistingUsername_StatusCodeConflict() {
        // Arange
        int existingId = 543;
        String existingEmail = "testUser@mail.com";
        String existingPassword = "password123";

        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";

        String roleName = "member";

        User existing = new User();
        existing.setId(existingId);
        existing.setUsername(username);
        existing.setEmail(existingEmail);
        existing.setPassword(existingPassword);

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        Role role = new Role();
        role.setId(id);
        role.setName(roleName);

        when(ur.getById(id)).thenReturn(user);
        when(ur.getByUsername(username)).thenReturn(existing);

        // Act
        ObjectResponse<User> response = userService.update(user);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        verify(ur, atLeastOnce()).getByUsername(username);
        assertEquals(HttpStatusCodes.CONFLICT, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void update_newWithExistingEmail_StatusCodeConflict() {
        // Arange
        int existingId = 543;
        String existingUsername = "testUser2";
        String existingPassword = "password123";

        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";

        String roleName = "member";

        User existing = new User();
        existing.setId(existingId);
        existing.setUsername(existingUsername);
        existing.setEmail(email);
        existing.setPassword(existingPassword);

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        Role role = new Role();
        role.setId(id);
        role.setName(roleName);

        when(ur.getById(id)).thenReturn(user);
        when(ur.getByUsername(username)).thenReturn(null);
        when(ur.getByEmail(email)).thenReturn(existing);

        // Act
        ObjectResponse<User> response = userService.update(user);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        verify(ur, atLeastOnce()).getByUsername(username);
        verify(ur, atLeastOnce()).getByEmail(email);
        assertEquals(HttpStatusCodes.CONFLICT, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void update_newWithBiographyToLong_StatusCodeNotAcceptable() {
        // Arange
        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "DiMg5G7a0WtzAMM2lZGtRDUxGPEzSz8IiTp9kdEFIjQv4Ry0fWIj6g954aJH3nR1tA9vphw3BPjtBtJrBevfSdItG1PNQp2ZSkXJ0ANeLPS03l0aw4X70I2adMZQ5rhKo7FYqszRiUYUsoLU5rt4viJ2UUVak08RHsqa3WErgUaqojWdx7vb2L4XDvCUDhzR4b56";

        String roleName = "member";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        Role role = new Role();
        role.setId(id);
        role.setName(roleName);

        when(ur.getById(id)).thenReturn(user);

        // Act
        ObjectResponse<User> response = userService.update(user);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * Delete User Tests
     */
    @Test
    public void delete_ExistingId_StatusCodeOk() {
        // Arange
        int id = 6;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);
        when(ur.getById(id)).thenReturn(user);
        when(ur.delete(user)).thenReturn(true);

        // Act
        ObjectResponse<User> response = userService.delete(user);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        verify(ur, atLeastOnce()).delete(user);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void delete_IdNull_StatusCodeNotAcceptable() {
        // Arange
        int id = 0;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        // Act
        ObjectResponse<User> response = userService.delete(user);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void delete_IdDoesNotExist_StatusCodeNotFound() {
        // Arange
        int id = 562;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);
        when(ur.getById(id)).thenReturn(null);

        // Act
        ObjectResponse<User> response = userService.delete(user);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * Follow User Tests
     */
    @Test
    public void follow_withExistingUsers_StatusCodeOk() {
        // Arange
        int id = 562;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        // Arange
        int id2 = 987;
        String username2 = "testUser2";
        String email2 = "testUser2@mail.com";
        String password2 = "password1433";
        String biography2 = "This is my biography2";

        User toFollow = new User();
        toFollow.setId(id2);
        toFollow.setUsername(username2);
        toFollow.setEmail(email2);
        toFollow.setPassword(password2);
        toFollow.setBiography(biography2);

        when(ur.getById(id)).thenReturn(user);
        when(ur.getById(id2)).thenReturn(toFollow);
        when(ur.follow(user, toFollow)).thenReturn(toFollow);

        // Act
        ObjectResponse<User> response = userService.follow(user, toFollow);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        verify(ur, atLeastOnce()).getById(id2);
        verify(ur, atLeastOnce()).follow(user, toFollow);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(toFollow, response.getObject());
    }

    @Test
    public void follow_UserIdNull_StatusCodeNotAcceptable() {
        // Arange
        int id = 0;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        // Arange
        int id2 = 987;
        String username2 = "testUser2";
        String email2 = "testUser2@mail.com";
        String password2 = "password1433";
        String biography2 = "This is my biography2";

        User toFollow = new User();
        toFollow.setId(id2);
        toFollow.setUsername(username2);
        toFollow.setEmail(email2);
        toFollow.setPassword(password2);
        toFollow.setBiography(biography2);

        // Act
        ObjectResponse<User> response = userService.follow(user, toFollow);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void follow_toFollowIdNull_StatusCodeNotAcceptable() {
        // Arange
        int id = 789;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        // Arange
        int id2 = 0;
        String username2 = "testUser2";
        String email2 = "testUser2@mail.com";
        String password2 = "password1433";
        String biography2 = "This is my biography2";

        User toFollow = new User();
        toFollow.setId(id2);
        toFollow.setUsername(username2);
        toFollow.setEmail(email2);
        toFollow.setPassword(password2);
        toFollow.setBiography(biography2);

        // Act
        ObjectResponse<User> response = userService.follow(user, toFollow);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void follow_UserDoesNotExist_StatusCodeOk() {
        // Arange
        int id = 562;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        // Arange
        int id2 = 987;
        String username2 = "testUser2";
        String email2 = "testUser2@mail.com";
        String password2 = "password1433";
        String biography2 = "This is my biography2";

        User toFollow = new User();
        toFollow.setId(id2);
        toFollow.setUsername(username2);
        toFollow.setEmail(email2);
        toFollow.setPassword(password2);
        toFollow.setBiography(biography2);

        when(ur.getById(id)).thenReturn(null);

        // Act
        ObjectResponse<User> response = userService.follow(user, toFollow);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void follow_toFollowDoesNotExist_StatusCodeOk() {
        // Arange
        int id = 562;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        // Arange
        int id2 = 987;
        String username2 = "testUser2";
        String email2 = "testUser2@mail.com";
        String password2 = "password1433";
        String biography2 = "This is my biography2";

        User toFollow = new User();
        toFollow.setId(id2);
        toFollow.setUsername(username2);
        toFollow.setEmail(email2);
        toFollow.setPassword(password2);
        toFollow.setBiography(biography2);

        when(ur.getById(id)).thenReturn(user);
        when(ur.getById(id2)).thenReturn(null);

        // Act
        ObjectResponse<User> response = userService.follow(user, toFollow);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        verify(ur, atLeastOnce()).getById(id2);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void follow_UserAlreadyFollowingTheFollower_StatusCodeOk() {
        // Arange
        int id = 562;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        // Arange
        int id2 = 987;
        String username2 = "testUser2";
        String email2 = "testUser2@mail.com";
        String password2 = "password1433";
        String biography2 = "This is my biography2";

        User toFollow = new User();
        toFollow.setId(id2);
        toFollow.setUsername(username2);
        toFollow.setEmail(email2);
        toFollow.setPassword(password2);
        toFollow.setBiography(biography2);

        user.addFollowing(toFollow);

        when(ur.getById(id)).thenReturn(user);
        when(ur.getById(id2)).thenReturn(toFollow);

        // Act
        ObjectResponse<User> response = userService.follow(user, toFollow);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        verify(ur, atLeastOnce()).getById(id2);
        assertEquals(HttpStatusCodes.FORBIDDEN, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * unFollow User Tests
     */
    @Test
    public void unfollow_withExistingUsers_StatusCodeOk() {
        // Arange
        int id = 562;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        // Arange
        int id2 = 987;
        String username2 = "testUser2";
        String email2 = "testUser2@mail.com";
        String password2 = "password1433";
        String biography2 = "This is my biography2";

        User toFollow = new User();
        toFollow.setId(id2);
        toFollow.setUsername(username2);
        toFollow.setEmail(email2);
        toFollow.setPassword(password2);
        toFollow.setBiography(biography2);

        user.addFollowing(toFollow);

        when(ur.getById(id)).thenReturn(user);
        when(ur.getById(id2)).thenReturn(toFollow);
        when(ur.unfollow(user, toFollow)).thenReturn(toFollow);

        // Act
        ObjectResponse<User> response = userService.unfollow(user, toFollow);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        verify(ur, atLeastOnce()).getById(id2);
        verify(ur, atLeastOnce()).unfollow(user, toFollow);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(toFollow, response.getObject());
    }

    @Test
    public void unfollow_UserIdNull_StatusCodeNotAcceptable() {
        // Arange
        int id = 0;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        // Arange
        int id2 = 987;
        String username2 = "testUser2";
        String email2 = "testUser2@mail.com";
        String password2 = "password1433";
        String biography2 = "This is my biography2";

        User toFollow = new User();
        toFollow.setId(id2);
        toFollow.setUsername(username2);
        toFollow.setEmail(email2);
        toFollow.setPassword(password2);
        toFollow.setBiography(biography2);

        user.addFollowing(toFollow);

        // Act
        ObjectResponse<User> response = userService.unfollow(user, toFollow);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void unfollow_toFollowIdNull_StatusCodeNotAcceptable() {
        // Arange
        int id = 789;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        // Arange
        int id2 = 0;
        String username2 = "testUser2";
        String email2 = "testUser2@mail.com";
        String password2 = "password1433";
        String biography2 = "This is my biography2";

        User toFollow = new User();
        toFollow.setId(id2);
        toFollow.setUsername(username2);
        toFollow.setEmail(email2);
        toFollow.setPassword(password2);
        toFollow.setBiography(biography2);

        user.addFollowing(toFollow);

        // Act
        ObjectResponse<User> response = userService.unfollow(user, toFollow);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void unfollow_UserDoesNotExist_StatusCodeOk() {
        // Arange
        int id = 562;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        // Arange
        int id2 = 987;
        String username2 = "testUser2";
        String email2 = "testUser2@mail.com";
        String password2 = "password1433";
        String biography2 = "This is my biography2";

        User toFollow = new User();
        toFollow.setId(id2);
        toFollow.setUsername(username2);
        toFollow.setEmail(email2);
        toFollow.setPassword(password2);
        toFollow.setBiography(biography2);

        user.addFollowing(toFollow);

        when(ur.getById(id)).thenReturn(null);

        // Act
        ObjectResponse<User> response = userService.unfollow(user, toFollow);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void unfollow_toFollowDoesNotExist_StatusCodeOk() {
        // Arange
        int id = 562;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        // Arange
        int id2 = 987;
        String username2 = "testUser2";
        String email2 = "testUser2@mail.com";
        String password2 = "password1433";
        String biography2 = "This is my biography2";

        User toFollow = new User();
        toFollow.setId(id2);
        toFollow.setUsername(username2);
        toFollow.setEmail(email2);
        toFollow.setPassword(password2);
        toFollow.setBiography(biography2);

        user.addFollowing(toFollow);

        when(ur.getById(id)).thenReturn(user);
        when(ur.getById(id2)).thenReturn(null);

        // Act
        ObjectResponse<User> response = userService.unfollow(user, toFollow);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        verify(ur, atLeastOnce()).getById(id2);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void unfollow_UserNotFollowingTheFollower_StatusCodeOk() {
        // Arange
        int id = 562;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);

        // Arange
        int id2 = 987;
        String username2 = "testUser2";
        String email2 = "testUser2@mail.com";
        String password2 = "password1433";
        String biography2 = "This is my biography2";

        User toFollow = new User();
        toFollow.setId(id2);
        toFollow.setUsername(username2);
        toFollow.setEmail(email2);
        toFollow.setPassword(password2);
        toFollow.setBiography(biography2);

        when(ur.getById(id)).thenReturn(user);
        when(ur.getById(id2)).thenReturn(toFollow);

        // Act
        ObjectResponse<User> response = userService.unfollow(user, toFollow);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        verify(ur, atLeastOnce()).getById(id2);
        assertEquals(HttpStatusCodes.FORBIDDEN, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * Login Tests
     */
    @Test
    public void login_CorrectDetails_StatusCodeOk() {
        // Arange
        int id = 562;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(PasswordAuthentication.hash(password));
        user.setBiography(biography);

        when(ur.getByUsername(username)).thenReturn(user);

        // Act
        ObjectResponse<User> response = userService.login(username, password);

        // Assert
        verify(ur, atLeastOnce()).getByUsername(username);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(user, response.getObject());
    }

    @Test
    public void login_IncorrectPassword_StatusCodeUnauthorized() {
        // Arange
        int id = 562;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(PasswordAuthentication.hash(password));
        user.setBiography(biography);

        when(ur.getByUsername(username)).thenReturn(user);

        // Act
        ObjectResponse<User> response = userService.login(username, "incorrectPassword");

        // Assert
        verify(ur, atLeastOnce()).getByUsername(username);
        assertEquals(HttpStatusCodes.UNAUTHORIZED, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void login_IncorrectUsername_StatusCodeUnauthorized() {
        // Arange
        int id = 562;
        String username = "testUser";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(PasswordAuthentication.hash(password));
        user.setBiography(biography);

        when(ur.getByUsername(username)).thenReturn(null);

        // Act
        ObjectResponse<User> response = userService.login(username, password);

        // Assert
        verify(ur, atLeastOnce()).getByUsername(username);
        assertEquals(HttpStatusCodes.UNAUTHORIZED, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void login_EmptyUsername_StatusCodeNotAcceptable() {
        // Arange
        int id = 562;
        String username = "";
        String email = "testUser@mail.com";
        String password = "password123";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(PasswordAuthentication.hash(password));
        user.setBiography(biography);


        // Act
        ObjectResponse<User> response = userService.login(username, password);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void login_EmptyPassword_StatusCodeNotAcceptable() {
        // Arange
        int id = 562;
        String username = "asdfasdf";
        String email = "testUser@mail.com";
        String password = "";
        String biography = "This is my biography";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(PasswordAuthentication.hash(password));
        user.setBiography(biography);

        // Act
        ObjectResponse<User> response = userService.login(username, password);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }
}
