import domain.Permission;
import domain.Role;
import domain.Tweet;
import domain.User;
import exceptions.CreationFailedException;
import exceptions.InvalidContentException;
import exceptions.NameNotUniqueException;
import exceptions.NotFoundException;
import responses.ObjectResponse;
import service.PermissionService;
import service.RoleService;
import service.TweetService;
import service.UserService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;

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
        try {
            permissionService.create(new Permission("create_user"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


        try {
            permissionService.create(new Permission("create_tweet"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
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
            user.setLatitude(000.000);
            user.setLongitude(000.000);
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
