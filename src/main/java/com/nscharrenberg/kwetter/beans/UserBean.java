package com.nscharrenberg.kwetter.beans;

import com.nscharrenberg.kwetter.domain.User;
import com.nscharrenberg.kwetter.responses.ObjectResponse;
import com.nscharrenberg.kwetter.service.UserService;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class UserBean implements Serializable {

    @Inject
    private UserService userService;

    private List<User> users;
    private User selectedUser;
    private List<User> filteredUser;

    @PostConstruct
    public void loadUsers() {
        ObjectResponse<List<User>> response = userService.all();

        if(response.getObject() != null) {
            this.users = response.getObject();
        } else {
            this.users = new ArrayList<>();
        }
    }

    public void create(String username, String email, String password, String biography, String website) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setBiography(biography);
        user.setWebsite(website);

        ObjectResponse<User> response = userService.create(user);

        if(response.getObject() != null) {
            Messages.create("User Created").detail(String.format("User with username %s has been created", user.getUsername())).add();
        } else {
            Messages.create(String.format("Error %s", response.getCode())).error().detail(response.getMessage()).add();
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<User> getFilteredUser() {
        return filteredUser;
    }

    public void setFilteredUser(List<User> filteredUser) {
        this.filteredUser = filteredUser;
    }
}
