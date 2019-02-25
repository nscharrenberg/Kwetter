/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.unit;

import model.Role;
import model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.interfaces.RoleRepository;
import repository.interfaces.UserRepository;
import repository.memory.RoleServiceImpl;
import repository.memory.UserServiceImpl;
import service.RoleService;
import service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldGetUserByUsername() {
        int id = 1;
        String username = "testUser";
        String biography = "This is my biography";
        String website = "www.site.com";
        User user = new User(id, username, "password", biography, 123.123123, 45.423, website, null);

        given(userRepository.getUserByUsername(username)).willReturn(user);

        assertThat(user.getUsername(), is(username));
        assertThat(user.getBiography(), is(biography));
    }

    @Test
    public void shouldGetUserById() {
        int id = 6;
        String username = "testUser";
        String biography = "This is my biography";
        String website = "www.site.com";
        User user = new User(6, username, "password", biography, 123.123123, 45.423, website, null);

        given(userRepository.getUserByUsername(username)).willReturn(user);

        assertThat(user.getId(), is(id));
        assertThat(user.getUsername(), is(username));
        assertThat(user.getBiography(), is(biography));
    }

    @Test
    public void createUser() {

        int id = 6;
        String username = "testUser";
        String biography = "This is my biography";
        String website = "www.site.com";
        Role role = new Role("member");
        User user = new User(username, "password", biography, 123.123123, 45.423, website, role);
        User result = null;
        //when
        try {
           userRepository.create(user);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception not expected during user creation");
        }

        //then
        assertThat(userRepository.getUserByUsername(username), is(user));
    }

    @Test
    public void updateUser() throws Exception {
        int id = 6;
        String username = "testUser";
        String biography = "This is my biography";
        String website = "www.site.com";
        Role role = new Role("member");
        User user = new User(6, username, "password", biography, 123.123123, 45.423, website, role);

        //given
        try {
            userRepository.create(user);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception not expected during user creation");
        }

        user.setWebsite("mysite");
        userRepository.update(user);

        //then
        assertThat(userRepository.getUserByUsername(username), is(user));
        assertThat(userRepository.getUserByUsername(username).getWebsite(), is("mysite"));
    }

    @Test
    public void followUser() {
        int id = 6;
        String username = "testUser";
        String biography = "This is my biography";
        String website = "www.site.com";
        Role role = new Role("member");
        User user = new User(6, username, "password", biography, 123.123123, 45.423, website, role);
        User user2 = new User(6 + 1, username+1, "password", biography, 123.123123, 45.423, website, role);

        //given
        try {
            userRepository.create(user);
            userRepository.create(user2);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception not expected during user creation");
        }

        userRepository.follow(user, user2);

        assertThat(userRepository.getUserById(id).getFollowing().contains(user2), is(true));
        assertThat(userRepository.getUserById(id + 1).getFollowers().contains(user), is(true));
    }

    @Test
    public void unfollowUser() {
        int id = 6;
        String username = "testUser";
        String biography = "This is my biography";
        String website = "www.site.com";
        Role role = new Role("member");
        User user = new User(6, username, "password", biography, 123.123123, 45.423, website, role);
        User user2 = new User(6 + 1, username+1, "password", biography, 123.123123, 45.423, website, role);


        try {
            userRepository.create(user);
            userRepository.create(user2);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception not expected during user creation");
        }

        userRepository.follow(user, user2);

        assertThat(userRepository.getUserById(id).getFollowing().contains(user2), is(true));
        assertThat(userRepository.getUserById(id + 1).getFollowers().contains(user), is(true));

        userRepository.unfollow(user, user2);

        assertThat(userRepository.getUserById(id).getFollowing().contains(user2), is(false));
        assertThat(userRepository.getUserById(id + 1).getFollowers().contains(user), is(false));
    }
}
