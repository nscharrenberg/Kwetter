package com.nscharrenberg.kwetter;

import com.nscharrenberg.kwetter.domain.Permission;
import com.nscharrenberg.kwetter.domain.Role;
import com.nscharrenberg.kwetter.domain.Tweet;
import com.nscharrenberg.kwetter.domain.User;
import com.nscharrenberg.kwetter.responses.ObjectResponse;
import com.nscharrenberg.kwetter.service.PermissionService;
import com.nscharrenberg.kwetter.service.RoleService;
import com.nscharrenberg.kwetter.service.TweetService;
import com.nscharrenberg.kwetter.service.UserService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.inject.Inject;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import java.util.HashSet;
import java.util.Set;


@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.xhtml",
                errorPage = "/login.xhtml",
                useForwardToLogin = false
        )
)
@Singleton
@Startup
@FacesConfig
@ApplicationScoped
public class StartUp {
    @Inject
    private UserService userService;

    @Inject
    private RoleService roleService;

    @Inject
    private PermissionService permissionService;

    @Inject
    private TweetService tweetService;

    @PostConstruct
    public void initData() {
        setPermissions();
        setRoles();
        setUsers();
//        setTweets();
    }

    /**
     * Pre-populate permissions.
     */
    private void setPermissions() {
        Set<String> permissions = new HashSet<>();
        permissions.add("permissions");
        permissions.add("roles");
        permissions.add("users");
        permissions.add("tweets");

        /**
         * Default CRUD Functionality
         */
        permissions.forEach(p -> {
            permissionService.create(new Permission(String.format("create_%s", p)));
            permissionService.create(new Permission(String.format("read_%s", p)));
            permissionService.create(new Permission(String.format("update_%s", p)));
            permissionService.create(new Permission(String.format("delete_%s", p)));
        });

        // Be able to change the role of a user
        permissionService.create(new Permission("changerole_user"));
        permissionService.create(new Permission("attach_permission"));
        permissionService.create(new Permission("detach_permission"));

        //Tweet Permissions
        permissionService.create(new Permission("like_tweet"));
        permissionService.create(new Permission("unlike_tweet"));
        permissionService.create(new Permission("follow_user"));
        permissionService.create(new Permission("unfollow_user"));
    }

    /**
     * Pre-populate roles
     */
    private void setRoles() {
        try {
            roleService.create(new Role("admin"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        try {
            roleService.create(new Role("member"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        try {
            roleService.create(new Role("moderator"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Pre-populate users
     */
    private void setUsers()  {
        try {
            // USER: Create admin user
            User user = new User();
            user.setUsername("admin");
            user.setPassword("password123");
            user.setEmail("admin@admin.com");
            user.setBiography("admin biography");
            user.setWebsite("www.admin.nl");
            user.setLatitude(51.4517676);
            user.setLongitude(5.480993799999965);
            user.setFirstname("admin");
            user.setLastname("admin");
            user.setAvatar("https://2eu.funnyjunk.com/pictures/Dolan+duck+profile+picture+feel+free+to+share+this+epic_97d32e_3518960.jpg");
            ObjectResponse<User> created = userService.create(user);

            if(created.getObject() != null) {
                ObjectResponse<Role> role = roleService.getByName("admin");

                if(role.getObject() != null) {
                    userService.changeRole(created.getObject(), role.getObject());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void setTweets() {
        try {
            for(int i = 0; i < 5; i++) {
                Tweet tweet = new Tweet();
                tweet.setMessage(String.format("This is my message with count %s", i));

                ObjectResponse<User> userResponse = userService.getByUsername("admin");
                if(userResponse.getObject() != null) {
                    tweet.setAuthor(userResponse.getObject());
                }

                tweetService.create(tweet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
