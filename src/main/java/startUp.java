import exception.StringToLongException;
import exception.UsernameNotUniqueException;
import model.Role;
import model.User;
import repository.interfaces.UserRepository;
import service.RoleService;
import service.UserService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/*
 * Copyright (c) 2019. Noah Scharrenberg
 */
@Singleton
@Startup
public class startUp {
    @Inject
    private UserService userService;

    @Inject
    private RoleService roleService;

    public startUp() {
    }

    @PostConstruct
    private void initData() {
        Role role = new Role("member");
        User user = new User("testUser", "password", "this is my biography", 123.654,123.1235, "www.site.com", role);
        User user2 = new User("testUser2", "password", "this is my biography", 123.654,123.1235, "www.site.com", role);
        User user3 = new User("testUser3", "password", "this is my biography", 123.654,123.1235, "www.site.com", role);
        User user4 = new User("testUser4", "password", "this is my biography", 123.654,123.1235, "www.site.com", role);
        User user5 = new User("testUser5", "password", "this is my biography", 123.654,123.1235, "www.site.com", role);

        try {
            roleService.create(role);
            userService.create(user);
            userService.create(user2);
            userService.create(user3);
            userService.create(user4);
            userService.create(user5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
