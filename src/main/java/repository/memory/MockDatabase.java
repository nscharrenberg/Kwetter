/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package repository.memory;

import model.Permission;
import model.Role;
import model.Tweet;
import model.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MockDatabase {
    protected List<User> users;
    protected List<Tweet> tweets;
    protected List<Role> roles;
    protected List<Permission> permissions;
    private static MockDatabase mockDatabase = new MockDatabase();

    public MockDatabase() {
        clearDatabase();
    }

    public static MockDatabase getInstance() {
        return mockDatabase;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    protected void clearDatabase() {
        users = new ArrayList<>();
        tweets = new ArrayList<>();
        roles = new ArrayList<>();
        permissions = new ArrayList<>();
    }
}
