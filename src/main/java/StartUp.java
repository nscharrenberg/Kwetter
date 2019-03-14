import domain.Permission;
import domain.Role;
import domain.User;
import exceptions.CreationFailedException;
import exceptions.InvalidContentException;
import exceptions.NameNotUniqueException;
import exceptions.NotFoundException;
import service.PermissionService;
import service.RoleService;
import service.UserService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;

@Singleton
@Startup
@ApplicationScoped
public class StartUp {
    @Inject
    private UserService userService;

    @Inject
    private RoleService roleService;

    @Inject
    private PermissionService permissionService;

    @PostConstruct
    public void initData() {
        setPermissions();
        setRoles();
        setUsers();
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
        } catch (InvalidContentException | CreationFailedException | NameNotUniqueException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        try {
            roleService.create(new Role("member"));
        } catch (InvalidContentException | CreationFailedException | NameNotUniqueException e) {
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
            userService.create(user);
        } catch (NoSuchAlgorithmException | NameNotUniqueException | InvalidContentException | CreationFailedException | NotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
